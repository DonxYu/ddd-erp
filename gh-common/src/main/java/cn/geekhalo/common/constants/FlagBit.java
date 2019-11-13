package cn.geekhalo.common.constants;

import java.util.Optional;

public enum FlagBit implements BaseEnum<FlagBit>{
    YES(1,"是"),
    NO(0,"否");

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

    FlagBit(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static Optional<FlagBit> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(FlagBit.class,code));
    }

}
