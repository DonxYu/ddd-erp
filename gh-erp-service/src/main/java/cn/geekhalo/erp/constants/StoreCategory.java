package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;

import java.util.Optional;

public enum  StoreCategory implements BaseEnum<StoreCategory> {

    STORE_COMPANY_PRODUCT(1,"公司库"),
    STORE_AGENT(2,"代理商库");
    @Getter
    private Integer code;
    @Getter
    private String msg;
    StoreCategory(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return msg;
    }

    public static Optional<StoreCategory> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(StoreCategory.class,code));
    }
}
