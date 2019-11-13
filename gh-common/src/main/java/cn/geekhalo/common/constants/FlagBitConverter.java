package cn.geekhalo.common.constants;

import javax.persistence.AttributeConverter;

public class FlagBitConverter implements AttributeConverter<FlagBit,Integer> {
    @Override
    public Integer convertToDatabaseColumn(FlagBit flagBit) {
        return flagBit.getCode();
    }

    @Override
    public FlagBit convertToEntityAttribute(Integer code) {
        return FlagBit.of(code).orElse(null);
    }
}
