package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.constants.ValidStatusConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.dto.product.ProductSpecificationInitDto;
import lombok.Data;
import lombok.Value;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "tb_product_specification")
@Data
@Entity
@GenVO
public class ProductSpecification extends AbstractEntity {

    @Column(name = "product_id")
    private Long productId;

    @Description(value = "排序值")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    @Description(value = "规格唯一编码")
    @Column(name = "spec_code")
    private String specCode;

    @Column(name = "product_spec_name")
    private String productSpecName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "warn_stock")
    private Integer warnStock;

    @Column(name = "valid_status")
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    @Column(name = "sale_status")
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus onlineStatus;


    @Column(name = "time_interval")
    @Description(value = "授权时间")
    private Integer timeInterval;

    public void doCreate(ProductSpecificationInitDto dto){
        setTimeInterval(Objects.isNull(dto.getTimeInterval())?0:dto.getTimeInterval());
        setOnlineStatus(ValidStatus.INVALID);
        setValidStatus(ValidStatus.INVALID);
        setProductId(dto.getProductId());
//        List<SerializeAttr> serializeAttrList = dto.getAttrs().stream().map(spec -> new SerializeAttr(spec.getAttrName(),spec.getAttrValue())).collect(Collectors.toList());
//        setAttrJson(JSON.toJSONString(serializeAttrList));
        setSortNum(1);
        setSpecCode(dto.getSpecCode());
        setProductSpecName(dto.getProductSpecName());
        setPrice(dto.getPrice());
        setWarnStock(dto.getWarnStock());
    }

    public void invalid(){
        if(Objects.equals(ValidStatus.INVALID,getValidStatus())){
            throw new BusinessException(ErrorMsg.StatusHasInvalid);
        }
        setValidStatus(ValidStatus.INVALID);
    }

    public void valid(){
        if(Objects.equals(ValidStatus.VALID,getValidStatus())){
            throw new BusinessException(ErrorMsg.StatusHasValid);
        }
        setValidStatus(ValidStatus.VALID);
    }

    public void onLine(){
        if(Objects.equals(ValidStatus.VALID,getOnlineStatus())){
            throw new BusinessException(ErrorMsg.StatusHasValid);
        }
        setOnlineStatus(ValidStatus.VALID);
    }

    public void offLine(){
        if(Objects.equals(ValidStatus.INVALID,getOnlineStatus())){
            throw new BusinessException(ErrorMsg.StatusHasInvalid);
        }
        setOnlineStatus(ValidStatus.INVALID);
    }



    @Value
    private static class SerializeAttr{
        private String k;
        private String v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductSpecification that = (ProductSpecification) o;
        return Objects.equals(sortNum, that.sortNum) &&
                Objects.equals(specCode, that.specCode) &&
                Objects.equals(productSpecName, that.productSpecName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sortNum, specCode, productSpecName);
    }
}
