package cn.geekhalo.codegen.creator;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenCreatorIngore {
}
