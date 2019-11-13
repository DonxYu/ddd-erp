package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.creator.GenCreator;
import cn.geekhalo.codegen.creator.GenCreatorIngore;
import cn.geekhalo.codegen.soaupdater.GenUpdater;
import cn.geekhalo.codegen.soaupdater.GenUpdaterIgnore;
import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.constants.ValidStatusConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.domain.product.creator.CategoryCreator;
import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "tb_product_category")
@GenCreator
@GenUpdater
@GenVO
public class ProductCategory extends AbstractEntity {

    @Column(name = "code")
    @Description(value = "分类编码")
    private String code;

    @Description(value = "分类名称")
    @Column(name = "name")
    private String name;

    @Description(value = "排序号")
    @Column(name = "sort_num")
    private Integer sortNum;

    @GenUpdaterIgnore
    @GenCreatorIngore
    @Column(name = "valid_status")
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    @GenCreatorIngore
    @Column(name="parent_id")
    private Long parentCategoryId;


    public void init(){
        setValidStatus(ValidStatus.VALID);
    }

    public void doCreate(CategoryCreator creator){
        creator.accept(this);
    }

    public void doSubCategoryCreate(CategoryCreator creator, Long parentCategoryId){
        creator.accept(this);
        setParentCategoryId(parentCategoryId);
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

    public String toString() {
        return this.getClass().getSimpleName() + "-" + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductCategory category = (ProductCategory) o;
        return Objects.equals(code, category.code) &&
                Objects.equals(name, category.name) &&
                Objects.equals(sortNum, category.sortNum) &&
                validStatus == category.validStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code, name, sortNum, validStatus);
    }
}
