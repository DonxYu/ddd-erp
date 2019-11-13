package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class GoodsTransferDto {
    @ApiModelProperty(value = "入库库房id")
    private Long inStoreId;
    @ApiModelProperty(value = "出库库房id")
    private Long outStoreId;
    @ApiModelProperty(value = "商品条码")
    private List<String> barCodes;
    @ApiModelProperty(value = "操作人")
    private String operateUser;
    @ApiModelProperty(value = "操作时间时间戳")
    private Long operateTime;
}
