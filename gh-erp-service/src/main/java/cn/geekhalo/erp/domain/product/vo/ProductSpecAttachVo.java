package cn.geekhalo.erp.domain.product.vo;

import cn.geekhalo.erp.domain.product.ProductSpecificationAttach;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductSpecAttachVo extends BaseProductSpecificationAttachVO {

    @Setter
    @Getter
    private String specName;

    private String deliveryFlagName;

    private Integer deliveryFlagCode;

    public ProductSpecAttachVo(ProductSpecificationAttach attach, String specName){
        super(attach);
        setSpecName(specName);
        this.deliveryFlagCode = attach.getDeliveryFlag().getCode();
        this.deliveryFlagName = attach.getDeliveryFlag().getName();
    }

}
