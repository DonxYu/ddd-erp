package cn.geekhalo.erp.dto.product;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class AvailableGoodBarDto {
    private Long specId;
    private Integer number;
    private Long storeId;
}
