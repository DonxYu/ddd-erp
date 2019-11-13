package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;
import java.util.Optional;

public enum WebControlType implements BaseEnum<WebControlType> {
    RADIO(1,"RADIO"),
    SELECT(2,"SELECT"),
    MULSELECT(3,"MULSELECT"),
    TEXT(4,"TEXT")
    ;
    @Getter
    private Integer code;
    @Getter
    private String msg;
    WebControlType(Integer code,String msg){
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

    public static Optional<WebControlType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(WebControlType.class,code));
    }
}
