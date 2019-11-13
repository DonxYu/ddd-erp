package cn.geekhalo.erp.dto.storehouse;

import cn.geekhalo.erp.constants.InOutDirectionType;
import cn.geekhalo.erp.constants.InOutOperationType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreateStoreRecordDto {
    private InOutDirectionType directionType;
    private InOutOperationType operationType;
    private Long batchId;
    private String remark;
    private Long goodsId;
    private String operateUser;
    private Long operateTime;
    private String providerName;
    private BigDecimal price;
}
