package cn.geekhalo.erp.domain.storehouse.vo;

import cn.geekhalo.erp.domain.storehouse.StoreInOutRecord;
import lombok.Data;

@Data
public class StoreRecordVo extends BaseStoreInOutRecordVO {

    private String storeName;
    private String specName;
    private String type;
    private String inOutDirectionTypeName;
    public StoreRecordVo(StoreInOutRecord record){
        super(record);
        this.type = record.getOperationType().name();
        this.inOutDirectionTypeName = record.getDirectionType().getName();
    }

    public StoreRecordVo(StoreInOutRecord record,String storeName,String specName){
        super(record);
        this.storeName = storeName;
        this.specName = specName;
        this.type = record.getOperationType().name();
        this.inOutDirectionTypeName = record.getDirectionType().getName();
    }
}
