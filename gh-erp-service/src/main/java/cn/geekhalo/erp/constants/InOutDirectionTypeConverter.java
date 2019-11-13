package cn.geekhalo.erp.constants;

import javax.persistence.AttributeConverter;

public class InOutDirectionTypeConverter implements AttributeConverter<InOutDirectionType,Integer> {
    @Override
    public Integer convertToDatabaseColumn(InOutDirectionType inOutDirectionType) {
        return inOutDirectionType.getCode();
    }

    @Override
    public InOutDirectionType convertToEntityAttribute(Integer code) {
        return InOutDirectionType.of(code).orElse(null);
    }
}
