package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;

import java.util.Optional;

public enum TraceType implements BaseEnum<TraceType> {
    IN_STORE(1,"入库"),
    OUT_STORE(2,"出库");
    @Getter
    private Integer code;
    @Getter
    private String msg;
    TraceType(Integer code,String msg){
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

    public static Optional<TraceType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(TraceType.class,code));
    }
}
