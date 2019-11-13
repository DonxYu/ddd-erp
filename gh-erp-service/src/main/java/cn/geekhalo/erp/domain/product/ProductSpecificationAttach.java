package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.constants.FlagBit;
import cn.geekhalo.common.constants.FlagBitConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tb_product_specification_attach")
@Data
@Entity
@GenVO
public class ProductSpecificationAttach extends AbstractEntity {

    @Column(name = "specification_id")
    private Long specificationId;

    @Column(name = "subSpecId")
    private Long subSpecId;

    @Column(name = "count")
    private Integer count;

    @Column(name = "delivery_flag")
    @Description(value = "是否独立发货")
    @Convert(converter = FlagBitConverter.class)
    private FlagBit deliveryFlag;


    public ProductSpecificationAttach(Long specificationId,Long subSpecId,Integer count,FlagBit deliveryFlag){
        setSpecificationId(specificationId);
        setSubSpecId(subSpecId);
        setCount(count);
        setDeliveryFlag(deliveryFlag);
    }

}
