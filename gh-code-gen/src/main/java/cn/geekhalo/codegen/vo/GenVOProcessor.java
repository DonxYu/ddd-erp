package cn.geekhalo.codegen.vo;

import cn.geekhalo.codegen.BaseProcessor;
import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@AutoService(Processor.class)
public class GenVOProcessor extends BaseProcessor<GenVO> {
    public GenVOProcessor() {
        super(GenVO.class);
    }

    @Override
    protected void foreachClass(GenVO genVO, Element element, RoundEnvironment roundEnv) {
        genVo(genVO, element);
    }

    private void genVo(GenVO genVO, Element element) {
        String className = "Base" + element.getSimpleName().toString() + "VO";
        String packageName = genVO.pkgName();
        if (StringUtils.isEmpty(packageName)){
            packageName = element.getEnclosingElement().toString() + ".vo";
        }
        String parentClassName = getParentClassName(genVO, element);

        Set<TypeAndName> typeAndNames = findPublicGetter(element);

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(TypeName.get(Serializable.class))
                .addAnnotation(Data.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        if (StringUtils.isNotEmpty(parentClassName)){
            ClassName parent = ClassName.bestGuess(parentClassName);
            typeSpecBuilder.superclass(parent);
        }


        MethodSpec.Builder cMethodSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(element.asType()), "source")
                .addModifiers(Modifier.PROTECTED);

        if (StringUtils.isNotEmpty(parentClassName)){
            cMethodSpecBuilder.addStatement("super(source)");
        }

        for (TypeAndName typeAndName : typeAndNames) {
            FieldSpec fieldSpec = FieldSpec.builder(typeAndName.getType(), typeAndName.getName(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Setter.class)
                            .addMember("value", "$T.PUBLIC", AccessLevel.class)
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

            String fieldName = typeAndName.getName().substring(0, 1).toUpperCase() + typeAndName.getName().substring(1, typeAndName.getName().length());

            cMethodSpecBuilder.addStatement("this.set$L(source.get$L())", fieldName, fieldName);

        }

        typeSpecBuilder.addMethod(cMethodSpecBuilder.build());
        typeSpecBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .build());

        createJavaFile(typeSpecBuilder, packageName);
    }

    private String getParentClassName(GenVO genVO, Element element) {
        String parent = genVO.parent();
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
                return genVO.pkgName() + "Base" + getSuperClassName(typeElement.getSuperclass().toString()) +"VO";
            }
            else {
                return convertToVO(superClass);
            }
        }
        return null;
    }

    protected Set<TypeAndName> findPublicGetter(Element element){
        Map<String, FieldConfig> configMap = findFields(element).stream()
                .map(variableElement -> toFieldConfig(variableElement))
                .collect(toMap(config->config.getName(), config->config));

        Set<ExecutableElement> getterMethods = findGetter(element);

        Set<TypeAndName> result = Sets.newHashSet();
        // 对getter方法进行处理
        Set<TypeAndName> getterMethodResult = getterMethods.stream()
                .filter(element1 -> element1.getModifiers().contains(Modifier.PUBLIC))
                .map(element1 -> {
                    String fieldName = getFieldNameFromGetter(element1.getSimpleName().toString());
                    FieldConfig fieldConfig = configMap.get(fieldName);
                    String fieldDescr = fieldConfig != null ? fieldConfig.getDescr() : "";
                    boolean fieldIgnore = fieldConfig != null ? fieldConfig.isIgnore() : false;
                    GenVOIgnore genVOIgnore = element1.getAnnotation(GenVOIgnore.class);
                    boolean ignore = fieldIgnore || (genVOIgnore != null);
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

        // 对lombok方法进行处理
        if (element.getAnnotation(Data.class) != null || element.getAnnotation(Value.class) != null){
            Set<TypeAndName> lomboxGetter = findFields(element).stream()
                    .filter(element1 -> {
                        String filedName = element1.getSimpleName().toString();
                        return getterMethodResult.stream().noneMatch(typeAndName -> typeAndName.getName().equals(filedName));
                    }).filter(element1 -> {
                        Getter getter = element1.getAnnotation(Getter.class);
                        return getter == null || getter.value() == AccessLevel.PUBLIC;
            }).filter(element3->!element3.getModifiers().contains(Modifier.STATIC))
                    .map(element1 -> {
                String fieldName = getFieldNameFromGetter(element1.getSimpleName().toString());
                FieldConfig fieldConfig = configMap.get(fieldName);
                String fieldDescr = fieldConfig != null ? fieldConfig.getDescr() : "";
                boolean fieldIgnore = fieldConfig != null ? fieldConfig.isIgnore() : false;
                if (fieldIgnore){
                    return null;
                }else {
                    return new TypeAndName(element1, fieldDescr);
                }
            }).filter(typeAndName -> typeAndName != null)
                    .collect(Collectors.toSet());
            result.addAll(lomboxGetter);
        }
        return result;
    }

    private FieldConfig toFieldConfig(VariableElement element){
        element.getModifiers().contains(Modifier.STATIC);
        String name = element.getSimpleName().toString();
        boolean ignore = element.getAnnotation(GenVOIgnore.class) != null;
        Description description = element.getAnnotation(Description.class);
        String descr = description == null ? "" : description.value();
        return new FieldConfig(name, ignore, descr);
    }



    private String convertToVO(String superClass) {
//        String pkgName = superClass.substring(0, superClass.lastIndexOf('.'));
//        String clsName = superClass.substring(superClass.lastIndexOf('.') + 1, superClass.length());
//        return pkgName + ".vo.Base" + clsName + "VO";
        return "cn.geekhalo.codegen.vo.BaseAbstractEntityVO";
    }


}
