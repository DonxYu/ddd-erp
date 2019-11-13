package cn.geekhalo.common.constants;


public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    Integer getCode();

    String getName();

    static<T extends Enum<T> & BaseEnum<T> > T parseByCode(Class<T> cls, Integer code){
        for (T t : cls.getEnumConstants()){
            if (t.getCode().intValue() == code.intValue()){
                return t;
            }
        }
        return null;
    }

}