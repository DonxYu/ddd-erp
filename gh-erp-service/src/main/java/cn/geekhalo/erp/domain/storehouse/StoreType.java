package cn.geekhalo.erp.domain.storehouse;

import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.constants.ValidStatusConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.erp.constants.ErrorMsg;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Data
@Table(name = "yd_store_type")
@GenVO
public class StoreType extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Description(value = "仓库编码")
    private String code;

    @Column(name = "valid_status")
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    public StoreType(String name,String code){
        this.code = code;
        this.name = name;
    }

    public void init(){
        setValidStatus(ValidStatus.VALID);
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
