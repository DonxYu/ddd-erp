package cn.geekhalo.erp.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class InitSpecificationDto {
    private final String specificationName;
    private final Long storeId;
    private final Long specificationId;
    private final Integer stock;
    private final BigDecimal totalPrice;
}
