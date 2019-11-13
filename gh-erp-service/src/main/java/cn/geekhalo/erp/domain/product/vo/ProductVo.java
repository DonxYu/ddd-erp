package cn.geekhalo.erp.domain.product.vo;

import cn.geekhalo.erp.domain.product.Product;
import lombok.Getter;

public class ProductVo extends BaseProductVO{
    @Getter
    private String  productTypeName;
    @Getter
    private String categoryName;
    public ProductVo(Product product, String productTypeName, String categoryName){
        super(product);
        this.productTypeName = productTypeName;
        this.categoryName = categoryName;
    }
}
