package cn.geekhalo.codegen.dto;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenDTO {
    String pkgName();
    boolean genSuperClassGetterAndSetter() default false;
    boolean genSuperClassIdGetterAndSetter() default false;
}
