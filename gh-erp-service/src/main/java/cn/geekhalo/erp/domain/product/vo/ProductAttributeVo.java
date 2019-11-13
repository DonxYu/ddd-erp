package cn.geekhalo.erp.domain.product.vo;

import cn.geekhalo.erp.domain.product.ProductAttribute;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

public class ProductAttributeVo extends BaseProductAttributeVO {

    @Getter
    private List<ProductAttributeValueVo> attrValues = Lists.newArrayList();

    public ProductAttributeVo(ProductAttribute attribute){
        super(attribute);
    }

    public ProductAttributeVo(ProductAttribute attribute, List<ProductAttributeValueVo> attrValues){
        super(attribute);
        this.attrValues = attrValues;
    }
}
