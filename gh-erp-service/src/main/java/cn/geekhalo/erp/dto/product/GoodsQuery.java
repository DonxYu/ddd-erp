package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class GoodsQuery {
    private Long storeId;
    private Long specId;
    private Integer status;
    private Long startTime;
    private Long endTime;
    private Integer goodsType;
}
