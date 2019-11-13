package cn.geekhalo.codegen.converter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenCodeBasedEnumConverter {
    String pkgName();
}
