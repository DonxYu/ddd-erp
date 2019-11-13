package cn.geekhalo.erp.util;

import cn.geekhalo.common.constants.BaseEnum;
import cn.geekhalo.common.exception.BusinessException;
import org.springframework.util.StringUtils;
public class BizExceptionUtils {
    private BizExceptionUtils(){}

    public static  void checkEmpty(String str, BaseEnum baseEnum){
        if(StringUtils.isEmpty(str)){
            throw new BusinessException(baseEnum);
        }
    }
}
