package cn.geekhalo.common.validator;

import lombok.Value;

@Value
public class ErrorBody {
    private String name;
    private String msg;
}
