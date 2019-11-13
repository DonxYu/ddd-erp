package cn.geekhalo.erp.dto.product;

import cn.geekhalo.erp.constants.WebControlType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import java.util.List;

@ApiModel
@Data
public class AttrBean {
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @Column(name = "attr_code")
    @ApiModelProperty(value = "属性编码")
    private String attrCode;

    @Column(name = "attr_desc")
    @ApiModelProperty(value = "属性描述")
    private String attrDesc;

    @ApiModelProperty(name = "sort_num")
    private Integer sortNum;

    @ApiModelProperty(value = "前端控件类型")
    private WebControlType controlType;

    @ApiModelProperty
    private List<String> attrValues;
}
