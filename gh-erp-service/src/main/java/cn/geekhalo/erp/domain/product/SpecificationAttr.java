package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "tb_specification_attr")
@NoArgsConstructor
@AllArgsConstructor
@GenVO
public class SpecificationAttr extends AbstractEntity {

    @Column(name = "product_spec_id")
    private Long productSpecId;

    @Column(name = "attr_id")
    @Description(value = "属性id")
    private Long attrId;

    @Description(value = "属性值id")
    @Column(name = "attr_value_id")
    private Long attrValueId;

    @Description(value = "属性名")
    @Column(name = "attrName")
    private String attrName;

    @Column(name = "attrValue")
    @Description(value = "属性值")
    private String attrValue;

    @Description(value = "属性编码")
    @Column(name = "attr_code")
    private String attrCode;

    public void create(Long productSpecId,Long attrId,Long attrValueId,String attrName,String attrValue,String attrCode){
        setProductSpecId(productSpecId);
        setAttrId(attrId);
        setAttrValueId(attrValueId);
        setAttrName(attrName);
        setAttrValue(attrValue);
        setAttrCode(attrCode);
    }


}
