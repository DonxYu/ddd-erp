package cn.geekhalo.erp.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class WaterFlowUtils {
    private WaterFlowUtils(){}
    static final Snowflake snowflake = IdUtil.createSnowflake(1, 1);
    public static Long nextWaterFlow(){
        return snowflake.nextId();
    }
}
