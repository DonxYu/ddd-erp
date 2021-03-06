package cn.geekhalo.codegen.soaupdater;

import cn.geekhalo.codegen.BaseProcessor;
import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVOProcessor;
import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@AutoService(Processor.class)
public class GenSoaUpdaterProcessor extends BaseProcessor<GenUpdater> {

    public GenSoaUpdaterProcessor() {
        super(GenUpdater.class);
    }

    @Override
    protected void foreachClass(GenUpdater genUpdater, Element element, RoundEnvironment roundEnv) {
//        String packageName = genUpdater.pkgName();
//        if (StringUtils.isEmpty(packageName)){
//            packageName = element.getEnclosingElement().toString() + ".updater";
//        }
        String packageName = element.getEnclosingElement().toString() ;
        String className = "Base" + element.getSimpleName().toString() + "SoaUpdater";

        String parentClassName = getParentClassName(genUpdater, element);

        Set<TypeAndName> publicSetter = findPublicSetter(element);

        TypeVariableName typeVariableName = TypeVariableName.get("T extends " + className);

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addTypeVariable(typeVariableName)
                .addAnnotation(Data.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        if (StringUtils.isNotEmpty(parentClassName)){
            ClassName parent = ClassName.bestGuess(parentClassName);
            TypeName typeName = TypeVariableName.get("T");
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(parent, typeName);
            typeSpecBuilder.superclass(parameterizedTypeName);
        }

        MethodSpec.Builder acceptMethodBuilder = MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(TypeName.get(element.asType()), "target")
                        .build());
        if (StringUtils.isNotEmpty(parentClassName)){
            acceptMethodBuilder.addStatement("super.accept(target)");
        }

        for (TypeAndName typeAndName : publicSetter) {
            String acceptMethodName = "accept" + typeAndName.getName().substring(0, 1).toUpperCase() + typeAndName.getName().substring(1, typeAndName.getName().length());
            String targetSetterName = "set" + typeAndName.getName().substring(0, 1).toUpperCase() + typeAndName.getName().substring(1, typeAndName.getName().length());
            acceptMethodBuilder.addStatement("this.$L(target::$L)", acceptMethodName, targetSetterName);

            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(DataOptional.class), typeAndName.getType());
            FieldSpec fieldSpec = FieldSpec.builder(parameterizedTypeName, typeAndName.getName(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Setter.class)
                            .addMember("value", "$T.PRIVATE", AccessLevel.class)
                            .build())
                    .addAnnotation(AnnotationSpec.builder(Getter.class)
                            .addMember("value", "$T.PUBLIC", AccessLevel.class)
                            .build())
                    .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                            .addMember("value", "$S",typeAndName.getDescription())
                            .addMember("name", "$S", typeAndName.getName())
                            .build())
                    .build();
            typeSpecBuilder.addField(fieldSpec);

            MethodSpec methodSpec = MethodSpec.methodBuilder(typeAndName.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeVariableName.get("T"))
                    .addParameter(typeAndName.getType(), typeAndName.getName())
                    .addStatement("this.$L = DataOptional.of($L)", typeAndName.getName(), typeAndName.getName())
                    .addStatement("return (T) this")
                    .build();
            typeSpecBuilder.addMethod(methodSpec);


            ParameterizedTypeName consumerTypeName = ParameterizedTypeName.get(ClassName.get(Consumer.class), typeAndName.getType());
            MethodSpec applyMethodSpec = MethodSpec.methodBuilder(acceptMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeVariableName.get("T"))
                    .addParameter(consumerTypeName, "consumer")
                    .addCode(CodeBlock.builder()
                            .add("if(this.$L != null){ \n", typeAndName.getName())
                            .add("\tconsumer.accept(this.$L.getValue());\n", typeAndName.getName())
                            .add("}\n")
                            .build())
                    .addStatement("return (T) this")
                    .build();
            typeSpecBuilder.addMethod(applyMethodSpec);
        }

        typeSpecBuilder.addMethod(acceptMethodBuilder.build());

        createJavaFile(typeSpecBuilder, packageName);
    }

    private Set<TypeAndName> findPublicSetter(Element element) {
        Map<String, FieldConfig> configMap = findFields(element).stream()
                .map(variableElement -> toFieldConfig(variableElement))
                .collect(toMap(config->config.getName(), config->config));

        Set<ExecutableElement> getterMethods = findSetter(element);

        Set<TypeAndName> result = Sets.newHashSet();
        // 对Setter方法进行处理
        Set<TypeAndName> getterMethodResult = getterMethods.stream()
                .filter(element1 -> !element1.getModifiers().contains(Modifier.PRIVATE))
                .map(element1 -> {
                    String fieldName = getFieldNameFromSetter(element1.getSimpleName().toString());
                    GenVOProcessor.FieldConfig fieldConfig = configMap.get(fieldName);
                    String fieldDescr = fieldConfig != null ? fieldConfig.getDescr() : "";
                    boolean fieldIgnore = fieldConfig != null ? fieldConfig.isIgnore() : false;

                    GenUpdaterIgnore genVOIgnore = element1.getAnnotation(GenUpdaterIgnore.class);
                    boolean ignore = fieldIgnore || (genVOIgnore != null) ;

                    Description description= element1.getAnnotation(Description.class);
                    String descr = description != null ? description.value() : fieldDescr;

                    if (ignore){
                        return null;
                    }else {
                        return new TypeAndName(element1, descr);
                    }
                }).filter(typeAndName -> typeAndName != null)
                .collect(Collectors.toSet());
        result.addAll(getterMethodResult);

        // 对lombok的Set方法进行处理
        if (element.getAnnotation(Data.class) != null){
            Set<TypeAndName> lombokSetter = findFields(element).stream()
                    .filter(element1 -> {
                        String filedName = element1.getSimpleName().toString();
                        return getterMethodResult.stream().noneMatch(typeAndName -> typeAndName.getName().equals(filedName));
                    }).filter(element1 -> {
                        Setter setter = element1.getAnnotation(Setter.class);
                        return setter == null || setter.value() != AccessLevel.PRIVATE;
                    }).map(element1 -> {
                        String fieldName = getFieldNameFromSetter(element1.getSimpleName().toString());
                        GenVOProcessor.FieldConfig fieldConfig = configMap.get(fieldName);
                        String fieldDescr = fieldConfig != null ? fieldConfig.getDescr() : "";
                        boolean fieldIgnore = fieldConfig != null ? fieldConfig.isIgnore() : false;
                        if (fieldIgnore){
                            return null;
                        }else {
                            return new TypeAndName(element1, fieldDescr);
                        }
                    }).filter(typeAndName -> typeAndName != null)
                    .collect(Collectors.toSet());
            result.addAll(lombokSetter);
        }
        return result;
    }

    private FieldConfig toFieldConfig(VariableElement element){
        String name = element.getSimpleName().toString();
        boolean ignore = element.getAnnotation(GenUpdaterIgnore.class) != null;
        Description description = element.getAnnotation(Description.class);
        String descr = description == null ? "" : description.value();
        return new FieldConfig(name, ignore, descr);
    }

    private String getParentClassName(GenUpdater genUpdater, Element element) {
        String parent = genUpdater.parent();
        if (StringUtils.isNotEmpty(parent)){
            return parent;
        }
        if (element instanceof TypeElement){
            TypeElement typeElement = (TypeElement) element;
            String superClass = typeElement.getSuperclass().toString();
            if (Object.class.getName().equals(superClass)){
                return null;
            }else if(!typeElement.getSuperclass().toString().contains("AbstractEntity")){
                //如果父类不是JpaAggregate 继续迭代
                //Set<TypeAndName> parentTypeAndNames = findPublicGetter(typeElement);
                //genVo(genVO,typeElement);
                return getPackageName(typeElement.getSuperclass().toString()) + ".Base" + getSuperClassName(typeElement.getSuperclass().toString()) +"Updater";
            }
            else {
                return convertToUpdater(superClass);
            }
        }
        return null;
    }

    private String convertToUpdater(String superClass) {
//        String pkgName = superClass.substring(0, superClass.lastIndexOf('.'));
//        String clsName = superClass.substring(superClass.lastIndexOf('.') + 1, superClass.length());
//        return pkgName + ".updater.Base" + clsName + "Updater";
        return "cn.geekhalo.codegen.soaupdater.BaseAbstractEntityUpdater";
    }


}
