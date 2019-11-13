package cn.geekhalo.erp.config;


import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.exception.SystemException;
import cn.geekhalo.common.model.JsonObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public JsonObject handleBusinessException(BusinessException e){
        return JsonObject.res(e.getMsg(),e.getData());
    }

    @ExceptionHandler(SystemException.class)
    public JsonObject handleSystemException(SystemException e){
        logger.error("系统异常",e);
        return JsonObject.fail(CodeEnum.SystemError);
    }

    @ExceptionHandler(Exception.class)
    public JsonObject handleException(Exception e){
        logger.error("系统异常",e);
        return JsonObject.fail(CodeEnum.SystemError);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public JsonObject handleJwtException(JwtException e){
        logger.error("token异常",e);
        return JsonObject.fail(CodeEnum.TokenExpired);
    }
}
