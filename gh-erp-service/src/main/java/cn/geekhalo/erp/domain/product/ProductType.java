package cn.geekhalo.erp.domain.product;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.constants.ValidStatusConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.erp.constants.ErrorMsg;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "tb_product_type")
@Data
@Entity
@QueryEntity
public class ProductType extends AbstractEntity {


    @Column(name = "type_name")
    private String typeName;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "valid_status")
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;


    public void init(){
        setValidStatus(ValidStatus.VALID);
    }

    public ProductType(String typeName,String typeCode){
        setValidStatus(ValidStatus.VALID);
        this.typeName = typeName;
        this.typeCode = typeCode;
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
