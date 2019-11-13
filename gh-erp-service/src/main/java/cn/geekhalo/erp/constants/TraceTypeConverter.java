package cn.geekhalo.erp.constants;

import javax.persistence.AttributeConverter;

public class TraceTypeConverter implements AttributeConverter<TraceType,Integer> {
    @Override
    public Integer convertToDatabaseColumn(TraceType traceType) {
        return traceType.getCode();
    }

    @Override
    public TraceType convertToEntityAttribute(Integer code) {
        return TraceType.of(code).orElse(null);
    }
}
