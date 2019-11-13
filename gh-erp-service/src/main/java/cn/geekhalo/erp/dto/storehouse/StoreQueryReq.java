package cn.geekhalo.erp.dto.storehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreQueryReq {
    private String storeName;
    private String contactUsername;
    private String contactPhone;
    @ApiModelProperty(value = "(1,\"零件库\"),\n" +
            "    (2,\"坏件库\"),\n" +
            "    (3,\"成品库\");")
    private Long storeTypeId;

    private Integer validStatus;
}
