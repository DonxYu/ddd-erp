package cn.geekhalo.erp.constants;

import javax.persistence.AttributeConverter;

public class StoreCategoryConverter implements AttributeConverter<StoreCategory,Integer> {
    @Override
    public Integer convertToDatabaseColumn(StoreCategory storeCategory) {
        return storeCategory.getCode();
    }

    @Override
    public StoreCategory convertToEntityAttribute(Integer code) {
        return StoreCategory.of(code).orElse(null);
    }
}
