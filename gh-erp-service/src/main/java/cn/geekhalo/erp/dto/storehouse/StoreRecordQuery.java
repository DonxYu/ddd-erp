package cn.geekhalo.erp.dto.storehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreRecordQuery {
    @ApiModelProperty(value = "仓库id")
    private Long storeId;
    @ApiModelProperty(value = "规格id")
    private Long specId;
    private Long startTime;
    private Long endTime;
}
