package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel
public class GoodsInStoreDto {

    @ApiModelProperty(value = "仓库id")
    private Long storeId;
    @ApiModelProperty(value = "供应商id")
    private Long providerId;
    @ApiModelProperty(value = "规格id")
    private Long productSpecId;
    @ApiModelProperty(value = "数量")
    private Integer totalCount;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    @ApiModelProperty(value = "条码list")
    private List<BarWrapper> barcodes;
    @ApiModelProperty(value = "操作人")
    private String operateUser;
    @ApiModelProperty(value = "操作时间时间戳")
    private Long operateTime;


}
