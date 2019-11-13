package cn.geekhalo.common.validator;


import jodd.vtor.ValidationConstraint;
import jodd.vtor.ValidationConstraintContext;
import org.apache.commons.validator.GenericValidator;

public class EmailConstraint implements ValidationConstraint<Email> {

	@Override
	public void configure(Email args) {
		
	}
	@Override
	public boolean isValid(ValidationConstraintContext context, Object value) {
		if(null==value){
			return true;
		}else{
			return GenericValidator.isEmail(value.toString());
		}
	}

}
