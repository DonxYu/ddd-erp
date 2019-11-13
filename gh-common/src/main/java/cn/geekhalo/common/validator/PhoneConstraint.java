package cn.geekhalo.common.validator;


import java.util.regex.Pattern;

import jodd.vtor.ValidationConstraint;
import jodd.vtor.ValidationConstraintContext;

public class PhoneConstraint implements ValidationConstraint<Phone> {

	@Override
	public void configure(Phone args) {
		
	}
	
	@Override
	public boolean isValid(ValidationConstraintContext context, Object value) {
		if(null==value){
			return true;
		}else{
			return Pattern.matches("^[1][0-9]{10}$", value.toString()) || Pattern.matches("^[0][0-9]{2,3}-[0-9]{5,10}$", value.toString());
		}
	}
}
