package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tb_product_attribute_value")
@Data
@Entity
@GenVO
public class ProductAttributeValue extends AbstractEntity {

    @Column(name = "product_attr_id")
    private Long productAttrId;

    @Description(value = "属性的值")
    private String attrValue;

    public ProductAttributeValue(Long productAttrId, String attrValue){
        this.productAttrId = productAttrId;
        this.attrValue = attrValue;
    }
}
