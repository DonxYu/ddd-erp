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
public class StoreCreateDto {
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
    
    @ApiModelProperty(value = "COMPONENT(1,\"零件库\"),\n" +
            "    BAD(2,\"坏件库\"),\n" +
            "    GOODS(3,\"成品库\");")
    private Long storeTypeId;
    
    @ApiModelProperty(value = "TORE_COMPANY_PRODUCT(1,\"公司产品库\"),\n" +
            "    STORE_INNER(2,\"内部库\"),\n" +
            "    STORE_COMPANY_GOODS(3,\"公司销售库\");")
    private Integer category;
    
    @ApiModelProperty(value = "规格id-list")
    private List<Long> specIds;

    @NotEmpty(message = "仓库编号不能为空")
    @MaxLength(value = 20,message = "仓库编号最长为20位")
    @ApiModelProperty(value = "仓库编号")
    private String storeCode;

    @ApiModelProperty(value = "仓库管理员")
    private List<StoreManager> managers;

    @Data
    public static class StoreManager{
        private Long userId;
        private String username;
    }
}
