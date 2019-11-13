package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * sku类
 */
@Data
@ApiModel
public class ProductSpecificationInitDto {
    @ApiModelProperty(value = "产品id")
    private Long productId;
    @ApiModelProperty(value = "属性值")
    List<AttrWrapper> attrs;
    @ApiModelProperty(value = "规格唯一编码")
    private String specCode;
    @ApiModelProperty(value = "规格名称")
    private String productSpecName;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    @ApiModelProperty(value = "报警库存")
    private Integer warnStock;
    @ApiModelProperty(value = "时间间隔")
    private Integer timeInterval;
    @ApiModelProperty(value = "日期类型，1年 2月 3天")
    private Integer timeType;
    @ApiModelProperty(value = "规格类型 1：基础件，2：生产件 3：配货件")
    private Integer specType;
}
