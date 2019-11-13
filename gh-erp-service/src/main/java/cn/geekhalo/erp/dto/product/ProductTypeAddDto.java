package cn.geekhalo.erp.dto.product;


import cn.geekhalo.common.validator.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import jodd.vtor.constraint.MaxLength;
import lombok.Data;

@Data
public class ProductTypeAddDto {
	@NotEmpty(message = "商品类型名称不能为空")
	@MaxLength(value = 100,message = "商品类型名称最长为100位")
    @ApiModelProperty(value = "商品类型名称")
    private String typeName;
	@NotEmpty(message = "商品类型编号不能为空")
	@MaxLength(value = 20,message = "商品类型编号最长为20位")
    @ApiModelProperty(value = "商品类型编号")
    private String typeCode;
}
