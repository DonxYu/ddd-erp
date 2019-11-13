package cn.geekhalo.common.constants;

import java.util.Optional;

public enum CodeEnum implements BaseEnum<CodeEnum> {

    Success(1,"操作成功"),
    Fail(0,"操作失败"),


    NotFindError(20000001,"未查询到信息"),
    SaveError(20000002,"保存信息失败"),
    UpdateError(20000003,"更新信息失败"),
    ValidateError(20000004,"数据检验失败"),
    TokenExpired(20000005,"令牌过期"),

    SystemError(10000001,"系统异常"),
    BusinessError(10000002,"业务异常"),
    ParamSetIllegal(10000003,"参数设置非法");
    private Integer code;
    private String msg;

    CodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.msg;
    }

    public static Optional<CodeEnum> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(CodeEnum.class,code));
    }
}
