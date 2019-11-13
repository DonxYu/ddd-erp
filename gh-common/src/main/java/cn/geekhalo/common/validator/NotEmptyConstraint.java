package cn.geekhalo.common.validator;

import jodd.util.StringUtil;
import jodd.vtor.ValidationConstraint;
import jodd.vtor.ValidationConstraintContext;

public class NotEmptyConstraint implements ValidationConstraint<NotEmpty>{
	public void configure(NotEmpty annotation) {
	}

	public boolean isValid(ValidationConstraintContext vcc, Object value) {
		return validate(value);
	}

	public static boolean validate(Object value) {
		return value == null ? false : StringUtil.isNotBlank(value.toString());
	}
}
	