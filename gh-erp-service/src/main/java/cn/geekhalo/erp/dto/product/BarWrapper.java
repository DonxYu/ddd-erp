package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BarWrapper {
    @ApiModelProperty(value = "条码")
    private String barCode;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
}
