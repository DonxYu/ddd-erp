package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;

import java.util.Optional;

public enum  SerializeType implements BaseEnum<SerializeType> {

    SERIALIZE(1,"序列化"),
    NONSERIALIZE(2,"非序列化");
    @Getter
    private Integer code;
    @Getter
    private String msg;
    SerializeType(Integer code,String msg){
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

    public static Optional<SerializeType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(SerializeType.class,code));
    }
}
