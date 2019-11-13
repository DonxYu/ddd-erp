package cn.geekhalo.common.constants;

import javax.persistence.AttributeConverter;

public class GoodsTypeConverter implements AttributeConverter<GoodsType,Integer> {
    @Override
    public Integer convertToDatabaseColumn(GoodsType goodsType) {
        return goodsType.getCode();
    }

    @Override
    public GoodsType convertToEntityAttribute(Integer code) {
        return GoodsType.of(code).orElse(null);
    }
}
