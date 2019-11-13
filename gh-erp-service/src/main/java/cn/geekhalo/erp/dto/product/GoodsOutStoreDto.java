package cn.geekhalo.erp.dto.product;

import cn.geekhalo.erp.constants.GoodsStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class GoodsOutStoreDto {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "用途")
    private GoodsStatus userFor;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "出库库房id")
    private Long outStoreId;
    @ApiModelProperty(value = "入库库房id")
    private Long inStoreId;
    @ApiModelProperty(value = "批次号")
    private Long batchId;
}
