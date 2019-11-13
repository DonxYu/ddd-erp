package cn.geekhalo.codegen.creator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenCreator {
    String parent() default "";
}
