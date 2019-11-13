package cn.geekhalo.common.constants;

import lombok.Getter;

import java.util.Optional;

public enum ValidStatus implements BaseEnum<ValidStatus> {
    VALID(1,"valid"),
    INVALID(0,"invalid")
    ;
    @Getter
    private Integer code;
    @Getter
    private String msg;
    ValidStatus(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return msg;
    }

    public static Optional<ValidStatus> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(ValidStatus.class,code));
    }
}
