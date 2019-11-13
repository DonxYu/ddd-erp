package cn.geekhalo.codegen.vo;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenVO {
    String pkgName() default "";

    String parent() default "";
}
