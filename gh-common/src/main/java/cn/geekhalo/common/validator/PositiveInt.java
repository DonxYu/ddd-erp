package cn.geekhalo.common.validator;

import jodd.vtor.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(PositiveIntConstraint.class)
public @interface PositiveInt {
	String[] profiles() default {};

	int severity() default 0;

	String message() default "jodd.vtor.constraint.NotEmptyl";
}
