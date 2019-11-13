package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.creator.GenCreatorIngore;
import cn.geekhalo.codegen.soaupdater.GenUpdaterIgnore;
import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.constants.ValidStatusConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.constants.SerializeType;
import cn.geekhalo.erp.constants.SerializeTypeConverter;
import cn.geekhalo.erp.dto.product.CreateProductDto;
import lombok.Data;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "tb_product")
@ToString(callSuper = true)
@GenVO
public class Product extends AbstractEntity {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "type_id")
    private Long productTypeId;

    @Column(name = "price")
    @Description(value = "指导价格")
    private BigDecimal price;

    @Convert(converter = ValidStatusConverter.class)
    @Column(name = "valid_status")
    @Description(value = "上下架状态")
    @GenCreatorIngore
    @GenUpdaterIgnore
    private ValidStatus validStatus;

    @Column(name = "serialize_type")
    @Description(value = "序列化类型")
    @Convert(converter = SerializeTypeConverter.class)
    private SerializeType serializeType;

    @Description(value = "保修时间--销售日期为准+天数")
    @Column(name = "valid_days")
    private Integer validDays;


    public void init(){
        setValidStatus(ValidStatus.VALID);
    }
    public void createProduct(CreateProductDto dto){
        setPrice(dto.getPrice());
        setValidDays(dto.getValidDays());
        setProductName(dto.getProductName());
        setSerializeType(dto.getSerializeType());
        setCategoryId(dto.getCategoryId());
        setProductTypeId(dto.getTypeId());
        setProductCode(dto.getProductCode());
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

}
