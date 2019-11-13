package cn.geekhalo.codegen.converter;

import cn.geekhalo.codegen.BaseProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.persistence.AttributeConverter;

@AutoService(Processor.class)
public class GenCodeBasedEnumConverterProcessor extends BaseProcessor<GenCodeBasedEnumConverter> {
    public GenCodeBasedEnumConverterProcessor() {
        super(GenCodeBasedEnumConverter.class);
    }

    @Override
    protected void foreachClass(GenCodeBasedEnumConverter genCodeBasedEnumConverter, Element element, RoundEnvironment roundEnv) {
        String className = "CodeBased" + element.getSimpleName() + "Converter";

        ParameterizedTypeName attributeConverter = ParameterizedTypeName.get(
                ClassName.get(AttributeConverter.class),
                TypeName.get(element.asType()), TypeName.INT.box());

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(attributeConverter)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);


        MethodSpec convertToDatabaseColumn = MethodSpec.methodBuilder("convertToDatabaseColumn")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(element.asType()), "i")
                .returns(TypeName.INT.box())
                .addStatement("return i == null ? null : i.getCode()")
                .build();
        typeSpecBuilder.addMethod(convertToDatabaseColumn);

        MethodSpec convertToEntityAttribute = MethodSpec.methodBuilder("convertToEntityAttribute")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.INT.box(), "i")
                .returns(TypeName.get(element.asType()))
                .addStatement("return i == null ? null : $T.parseByCode(i)", TypeName.get(element.asType()))
                .build();
        typeSpecBuilder.addMethod(convertToEntityAttribute);

        createJavaFile(typeSpecBuilder, genCodeBasedEnumConverter.pkgName());


    }
}
