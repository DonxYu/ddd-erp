package cn.geekhalo.erp.dto.product;

import cn.geekhalo.common.validator.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import jodd.vtor.constraint.MaxLength;
import lombok.Data;

@Data
public class ProductCategoryAddDto {
	@NotEmpty(message = "排序不能为空")
    private Integer sortNum;
    
    @NotEmpty(message = "商品分类名称不能为空")
	@MaxLength(value = 100,message = "商品分类名称最长为100位")
    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;
    
    @NotEmpty(message = "商品分类编号不能为空")
	@MaxLength(value = 20,message = "商品分类编号最长为20位")
    @ApiModelProperty(value = "商品分类编号")
    private String categoryCode;

    private Long pid;

}
