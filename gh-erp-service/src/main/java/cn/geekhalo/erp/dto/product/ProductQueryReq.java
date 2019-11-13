package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ProductQueryReq {
    @ApiModelProperty(value = "产品名称")
    private String productName;
    private Long categoryId;
    private Long productTypeId;
}
