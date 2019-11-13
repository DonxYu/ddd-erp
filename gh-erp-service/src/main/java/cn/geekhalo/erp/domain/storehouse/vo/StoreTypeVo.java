package cn.geekhalo.erp.domain.storehouse.vo;

import cn.geekhalo.erp.domain.storehouse.StoreType;
import lombok.Data;

@Data
public class StoreTypeVo extends BaseStoreTypeVO{
    public StoreTypeVo(StoreType type){
        super(type);
    }
}
