package cn.geekhalo.codegen.soaupdater;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenUpdaterIgnore {
}
