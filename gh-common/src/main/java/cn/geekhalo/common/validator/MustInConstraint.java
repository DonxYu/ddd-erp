package cn.geekhalo.common.validator;

import jodd.vtor.ValidationConstraint;
import jodd.vtor.ValidationConstraintContext;

import java.util.Arrays;

public class MustInConstraint implements ValidationConstraint<MustIn>{
	protected String[] strs;
	@Override
	public void configure(MustIn must) {
		strs = must.value();
	}

	@Override
	public boolean isValid(ValidationConstraintContext context, Object value) {
		if(null==value){
			return true;
		}else{
			return Arrays.asList(strs).contains(value.toString());
		}
	}

}
