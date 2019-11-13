package cn.geekhalo.codegen.dto;

import cn.geekhalo.codegen.BaseProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@AutoService(Processor.class)
public class GenDTOProcessor extends BaseProcessor<GenDTO> {
    public GenDTOProcessor() {
        super(GenDTO.class);
    }

    @Override
    protected void foreachClass(GenDTO genDTO, Element element, RoundEnvironment roundEnv) {
        String className = "Base" + element.getSimpleName().toString() + "DTO";

        Set<TypeAndName> typeAndNames = findFields(element, filterForIgorne(GenDTOIgnore.class));
        typeAndNames.addAll(findGetter(element, filterForIgorne(GenDTOIgnore.class)));

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(TypeName.get(Serializable.class))
                .addAnnotation(Data.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);


        MethodSpec.Builder cMethodSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(element.asType()), "source")
                .addModifiers(Modifier.PROTECTED);
        if(genDTO.genSuperClassGetterAndSetter()){
            typeAndNames.add(new TypeAndName("id", TypeName.LONG.box()));
            typeAndNames.add(new TypeAndName("createTime", TypeName.get(Instant.class)));
            typeAndNames.add(new TypeAndName("updateTime", TypeName.get(Instant.class)));
        }
        if(genDTO.genSuperClassIdGetterAndSetter()){
            typeAndNames.add(new TypeAndName("id", TypeName.LONG.box()));
        }
        //typeAndNames.add( new TypeAndName("version",TypeName.LONG.box()));
        for (TypeAndName typeAndName : typeAndNames) {
            FieldSpec fieldSpec = FieldSpec.builder(typeAndName.getType(), typeAndName.getName(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Setter.class)
                            .addMember("value", "$T.PRIVATE", AccessLevel.class)
                            .build())
                    .addAnnotation(AnnotationSpec.builder(Getter.class)
                            .addMember("value", "$T.PUBLIC", AccessLevel.class)
                            .build())
                    .build();
            typeSpecBuilder.addField(fieldSpec);

            String fieldName = typeAndName.getName().substring(0, 1).toUpperCase() + typeAndName.getName().substring(1, typeAndName.getName().length());

            cMethodSpecBuilder.addStatement("this.set$L(source.get$L())", fieldName, fieldName);

//            MethodSpec getterMethodSpec = MethodSpec.methodBuilder(
//                    createGetter(typeAndName.getName(), typeAndName.getType())
//            )
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(typeAndName.getType())
//                    .addStatement("return this.$L", typeAndName.getName())
//                    .build();
//            typeSpecBuilder.addMethod(getterMethodSpec);
        }


        typeSpecBuilder.addMethod(cMethodSpecBuilder.build());
        String pkgName = genDTO.pkgName();

        createJavaFile(typeSpecBuilder, pkgName);

    }

//    private String createGetter(String name, TypeName typeName) {
//        if (typeName.toString() ==)
//        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
//    }
}
