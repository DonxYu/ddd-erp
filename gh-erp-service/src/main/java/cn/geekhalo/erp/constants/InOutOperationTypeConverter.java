package cn.geekhalo.erp.constants;

import javax.persistence.AttributeConverter;

public class InOutOperationTypeConverter implements AttributeConverter<InOutOperationType,Integer> {
    @Override
    public Integer convertToDatabaseColumn(InOutOperationType inOutOperationType) {
        return inOutOperationType.getCode();
    }

    @Override
    public InOutOperationType convertToEntityAttribute(Integer code) {
        return InOutOperationType.of(code).orElse(null);
    }
}
