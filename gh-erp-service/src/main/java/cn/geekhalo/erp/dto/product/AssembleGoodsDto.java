package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AssembleGoodsDto {
    @ApiModelProperty(value = "仓库id")
    private Long storeId;
    @ApiModelProperty(value = "父类条码")
    private String parentBarCode;
    private String operateUser;
    @ApiModelProperty(value = "子类条码")
    private List<String> sonBarCodes;
}
