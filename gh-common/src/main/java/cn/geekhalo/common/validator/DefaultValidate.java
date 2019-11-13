package cn.geekhalo.common.validator;

import lombok.Data;
import org.assertj.core.util.Lists;

import java.util.List;

@Data
public class DefaultValidate implements Validate {

    private boolean pass;

    private List<ErrorBody> errorList = Lists.newArrayList();

    @Override
    public boolean pass() {
        return pass;
    }

    @Override
    public List<ErrorBody> errors() {
        return errorList;
    }


}
