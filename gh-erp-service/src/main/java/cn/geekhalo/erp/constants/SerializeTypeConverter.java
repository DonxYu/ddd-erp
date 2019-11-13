package cn.geekhalo.erp.constants;

import javax.persistence.AttributeConverter;

public class SerializeTypeConverter implements AttributeConverter<SerializeType,Integer> {
    @Override
    public Integer convertToDatabaseColumn(SerializeType serializeType) {
        return serializeType.getCode();
    }

    @Override
    public SerializeType convertToEntityAttribute(Integer code) {
        return SerializeType.of(code).orElse(null);
    }
}
