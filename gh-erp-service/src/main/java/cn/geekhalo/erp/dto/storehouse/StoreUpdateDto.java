package cn.geekhalo.erp.dto.storehouse;

import cn.geekhalo.common.validator.NotEmpty;
import cn.geekhalo.common.validator.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jodd.vtor.constraint.MaxLength;
import lombok.Data;

import java.util.List;


@Data
@ApiModel
public class StoreUpdateDto {
	@NotEmpty(message = "仓库id不能为空")
    @ApiModelProperty(value = "仓库id")
    private Long id;
	
	@NotEmpty(message = "仓库名称不能为空")
	@MaxLength(value = 100,message = "仓库名称最长为100位")
    @ApiModelProperty(value = "仓库名称")
    private String storeName;
	
	@NotEmpty(message = "仓库地址不能为空")
    @MaxLength(value = 200,message = "仓库地址最长为200位")
	@ApiModelProperty(value = "仓库地址")
    private String address;
	
	@NotEmpty(message = "仓库联系人不能为空")
	@MaxLength(value = 10,message = "仓库联系人最长为10位")
    @ApiModelProperty(value = "仓库联系人")
    private String contactUsername;
	
	@NotEmpty(message = "仓库电话不能为空")
    @Phone(message = "仓库电话格式不正确")
    @ApiModelProperty(value = "仓库电话")
    private String contactPhone;
	
    @ApiModelProperty(value = "规格id-list")
    private List<Long> specIds;
}
