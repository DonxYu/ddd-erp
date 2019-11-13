package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;

import java.util.Optional;

public enum  GoodsStatus implements BaseEnum<GoodsStatus> {

    INIT(1,"初始入库"),
    BEUSED(2,"组装使用"),
    TRANSFER(3,"库间转移"),
    SALED(4,"已出售"),
    MAKE_USED(5,"生产使用"),
    ASSEMBLE_USED(6,"配货使用"),
    ;
    @Getter
    private Integer code;
    @Getter
    private String msg;
    GoodsStatus(Integer code,String msg){
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

    public static Optional<GoodsStatus> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(GoodsStatus.class,code));
    }
}
