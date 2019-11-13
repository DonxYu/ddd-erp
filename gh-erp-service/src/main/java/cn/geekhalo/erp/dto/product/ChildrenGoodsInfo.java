package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class ChildrenGoodsInfo {
    private Long specId;
    private String barCode;
}
