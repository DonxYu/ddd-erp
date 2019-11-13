package cn.geekhalo.common.constants;

import lombok.Getter;

import java.util.Optional;

public enum GoodsType implements BaseEnum<GoodsType> {
    GOODS_BASIC(1,"零部件"),
    GOODS_MAKE(2,"生产制造商品"),
    GOODS_ASSEMBLE(3,"配货商品"),
    GOODS_BAD(4,"坏件");
    @Getter
    private Integer code;
    @Getter
    private String msg;
    GoodsType(Integer code, String msg){
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

    public static Optional<GoodsType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(GoodsType.class,code));
    }
}
