package cn.geekhalo.erp.constants;

import javax.persistence.AttributeConverter;

public class GoodsStatusConverter implements AttributeConverter<GoodsStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(GoodsStatus goodsStatus) {
        return goodsStatus.getCode();
    }

    @Override
    public GoodsStatus convertToEntityAttribute(Integer code) {
        return GoodsStatus.of(code).orElse(null);
    }
}
