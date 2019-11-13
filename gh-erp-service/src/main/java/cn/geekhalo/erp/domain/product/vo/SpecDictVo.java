package cn.geekhalo.erp.domain.product.vo;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class SpecDictVo {
    private String specName;
    private Long id;
    private BigDecimal price;
}
