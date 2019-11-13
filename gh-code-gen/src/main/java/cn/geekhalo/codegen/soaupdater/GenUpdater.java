package cn.geekhalo.codegen.soaupdater;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenUpdater {
    //String pkgName() default "";

    String parent() default "";
}
