package cn.geekhalo.erp.domain.storehouse;

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
import cn.geekhalo.erp.constants.StoreCategory;
import cn.geekhalo.erp.constants.StoreCategoryConverter;
import cn.geekhalo.erp.domain.product.creator.StoreCreator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Table(name = "tb_store")
@Entity
@Data
@GenCreator
@GenUpdater
@GenVO
@ToString(callSuper = true)
public class Store extends AbstractEntity {

    @Setter(AccessLevel.PROTECTED)
    @Column(name = "store_house_name")
    @Description("仓库名称")
    private String storeHouseName;

    @Setter(AccessLevel.PROTECTED)
    @Column(name = "address")
    @Description("仓库地址")
    private String address;

    @Column(name = "contact_user_name")
    @Description("联系人姓名")
    private String contactUsername;

    @Description("联系人电话")
    @Column(name = "contact_phone")
    private String contactPhone;

    @Description("有效状态")
    @Column(name = "valid_status")
    @Convert(converter = ValidStatusConverter.class)
    @GenUpdaterIgnore
    @GenCreatorIngore
    private ValidStatus validStatus;

    @Column(name = "store_type_id")
    private Long storeTypeId;

    @Column(name = "storeCode")
    @GenUpdaterIgnore
    private String storeCode;

    @Column(name = "store_category")
    @Convert(converter = StoreCategoryConverter.class)
    private StoreCategory storeCategory;

    public void init(){
        setValidStatus(ValidStatus.VALID);
    }

    public void doCreateStore(StoreCreator creator){
        setStoreCode(String.valueOf(Instant.now().toEpochMilli()));
        creator.accept(this);
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
        Store store = (Store) o;
        return Objects.equals(storeHouseName, store.storeHouseName) &&
                Objects.equals(address, store.address) &&
                Objects.equals(contactUsername, store.contactUsername) &&
                Objects.equals(contactPhone, store.contactPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), storeHouseName, address, contactUsername, contactPhone);
    }
}
