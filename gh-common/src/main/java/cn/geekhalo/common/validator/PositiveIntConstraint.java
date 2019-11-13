package cn.geekhalo.common.validator;

import jodd.vtor.ValidationConstraint;
import jodd.vtor.ValidationConstraintContext;

public class PositiveIntConstraint implements ValidationConstraint<PositiveInt>{
	public void configure(PositiveInt annotation) {
	}

	public boolean isValid(ValidationConstraintContext vcc, Object value) {
		return validate(value);
	}

	public static boolean validate(Object value) {
		return value == null ? false :isPositiveInt(value.toString());
	}
	private static boolean isPositiveInt(String value){
		boolean flag = false;
		try{
			int a = Integer.parseInt(value);
			if(a>0){
				flag = true;
			}
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
}
