package cn.geekhalo.common.validator;

import jodd.vtor.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(NotEmptyConstraint.class)
public @interface NotEmpty {
	String[] profiles() default {};

	int severity() default 0;

	String message() default "jodd.vtor.constraint.NotEmptyl";
}
