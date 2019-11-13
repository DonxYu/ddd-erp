package cn.geekhalo.common.constants;

import java.util.Optional;

public enum  MakeType implements BaseEnum<MakeType>{
    MAKE(2,"生产"),
    ASSEMBLE(1,"组装");
    private Integer code;
    private String msg;
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return msg;
    }

    MakeType(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static Optional<MakeType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(MakeType.class,code));
    }
}
