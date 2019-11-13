package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@ApiModel
@Data
public class GoodsMakeDto {
    @ApiModelProperty(value = "仓库id")
    private Long storeId;
    private Long specId;
    @ApiModelProperty(value = "新的条码")
    private String goodsBarCode;
    @ApiModelProperty
    private List<ChildrenGoodsInfo> childrenGoodsInfos;
    private String operateUser;
}
