package cn.geekhalo.erp.domain.product.vo;

import cn.geekhalo.erp.domain.product.ProductSpecification;
import lombok.Data;
import lombok.Getter;

@Data
public class ProductSpecificationVo extends BaseProductSpecificationVO {

    private Integer hasChildren;
    @Getter
    private String  productTypeName;
    @Getter
    private String categoryName;
    @Getter
    private String productName;
    public ProductSpecificationVo(ProductSpecification specification, String productTypeName, String categoryName, String productName){
        super(specification);
        this.productTypeName = productTypeName;
        this.categoryName = categoryName;
        this.productName = productName;
    }

    public ProductSpecificationVo(ProductSpecification specification){
        super(specification);
    }


}
