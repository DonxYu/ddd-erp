package cn.geekhalo.erp.constants;


import cn.geekhalo.common.constants.BaseEnum;

import java.util.Optional;

public enum  ErrorMsg implements BaseEnum<ErrorMsg> {
    ProductTypeNotNull(110000100,"商品分类不能为空"),
    BarCodeNotNull(110000101,"条码不能为空"),
    BarCodeSizeNotEqualCount(110000102,"条码个数与产品数量不符合"),
    BarCodeDuplicate(110000103,"条码重复"),
    StatusNotSuit(110000104,"状态设置不正确"),
    StockNotEnough(110000105,"库存不足"),
    GoodsNotAvailable(110000106,"当前商品不可用"),
    ParamShouldNotEmpty(110000107,"参数不能为空"),
    StatusHasValid(110000108,"状态已经被启用"),
    StatusHasInvalid(110000109,"状态已经被禁用"),
    StoreNotExist(110000110,"库房没找到"),
    ProductNotExist(110000111,"产品没找到"),
    GoodsNotExist(110000112,"商品没找到"),
    SpecCodeIsExist(110000113,"规格编码已经存在"),
    ReplaceBarCodeRepeated(11000138,"有重复的条码"),
    StoreNameIsExist(11000139,"仓库名称已经存在"),
    SpecIsNotCorrect(11000140,"规格设置不正确")
    ;

    private Integer code;
    private String msg;

    ErrorMsg(Integer code,String msg){
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

    public static Optional<ErrorMsg> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(ErrorMsg.class,code));
    }
}
