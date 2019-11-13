package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SpecAttachInfo {
    private Long specId;
    private Integer count;
    @ApiModelProperty(value = "是否独立发货，1：是 0：否")
    private Integer deliveryFlag;
}
