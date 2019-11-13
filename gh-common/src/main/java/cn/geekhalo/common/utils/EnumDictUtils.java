package cn.geekhalo.common.utils;


        import cn.geekhalo.common.constants.BaseEnum;
        import cn.geekhalo.common.constants.EnumDict;

        import java.util.EnumSet;
        import java.util.List;
        import java.util.stream.Collectors;

public class EnumDictUtils {
    private EnumDictUtils(){}
    public static <T extends Enum<T> & BaseEnum<T>> List<EnumDict> getEnumDicts(Class<T> cls){
        return EnumSet.allOf(cls).stream().map(et -> new EnumDict(et.getName(), et.getCode())).collect(Collectors.toList());
    }
}
