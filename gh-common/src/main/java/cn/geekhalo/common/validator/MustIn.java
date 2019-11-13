package cn.geekhalo.common.validator;

import jodd.vtor.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(MustInConstraint.class)
public @interface MustIn {
	String [] value() default {"0","1"};

	String[] profiles() default {};

	int severity() default 0;

	String message() default "jodd.vtor.constraint.MustIn";
}
