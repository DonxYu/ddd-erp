package cn.geekhalo.erp.dto.product;

import cn.geekhalo.erp.constants.SerializeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class CreateProductDto {

    @ApiModelProperty(value = "商品编号")
    private String productCode;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "类型id")
    private Long typeId;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "保修期")
    private Integer validDays;

    @ApiModelProperty(value = "序列化类型")
    private SerializeType serializeType;

    private List<AttrBean> attrList;

}
