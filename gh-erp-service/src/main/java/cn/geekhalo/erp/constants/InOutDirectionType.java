package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;

import java.util.Optional;

public enum InOutDirectionType implements BaseEnum<InOutDirectionType> {
    IN(1,"入库"),
    OUT(2,"出库");
    @Getter
    private Integer code;
    @Getter
    private String msg;
    InOutDirectionType(Integer code,String msg){
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

    public static Optional<InOutDirectionType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(InOutDirectionType.class,code));
    }
}
