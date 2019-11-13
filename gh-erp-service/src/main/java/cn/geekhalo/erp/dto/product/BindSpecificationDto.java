package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel
public class BindSpecificationDto {
    @ApiModelProperty(value = "规格id")
    private Long specificationId;
    @ApiModelProperty(value = "仓库id")
    private Long storeId;
    @ApiModelProperty(value = "提供商Id")
    private Long providerId;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
}
