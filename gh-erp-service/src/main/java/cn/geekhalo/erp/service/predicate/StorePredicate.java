package cn.geekhalo.erp.service.predicate;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.qsl.AbstractPredicate;
import cn.geekhalo.erp.constants.StoreCategory;
import cn.geekhalo.erp.domain.storehouse.QStore;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class StorePredicate {

    public static final QStore Q_STORE = QStore.store;
    private static final StringPath STORE_NAME = Q_STORE.storeHouseName;
    private static final StringPath CONTACT_PHONE = Q_STORE.contactPhone;
    private static final StringPath CONTACT_USERNAME = Q_STORE.contactUsername;
    private static final EnumPath VALID_STATUS = Q_STORE.validStatus;
    private static final NumberPath STORE_TYPE = Q_STORE.storeTypeId;

    public static class StoreNamePredicate extends AbstractPredicate {

        private final String storeName;

        public StoreNamePredicate(String storeName){
            this.storeName = storeName;
        }

        @Override
        public Predicate getPredicate() {
            return STORE_NAME.like("%"+storeName+"%");
        }
    }

    public static class StoreCategoryPredicate extends AbstractPredicate{
        private Integer categoryCode;
        public StoreCategoryPredicate(Integer categoryCode){
            this.categoryCode = categoryCode;
        }
        @Override
        public Predicate getPredicate() {
            return Q_STORE.storeCategory.eq(StoreCategory.of(categoryCode).orElse(StoreCategory.STORE_COMPANY_PRODUCT));
        }
    }

    public static class ContactUsernamePredicate extends AbstractPredicate{

        private final String contactName;

        public ContactUsernamePredicate(String contactName){
            this.contactName = contactName;
        }

        @Override
        public Predicate getPredicate() {
            return CONTACT_USERNAME.like("%"+contactName+"%");
        }
    }

    public static class ContactPhonePredicate extends AbstractPredicate{

        private final String contactPhone;

        public ContactPhonePredicate(String contactPhone){
            this.contactPhone = contactPhone;
        }

        @Override
        public Predicate getPredicate() {
            return CONTACT_PHONE.like("%"+contactPhone+"%");
        }
    }


    public static class StoreStatusPredicate extends AbstractPredicate{
        private final Integer status;
        public StoreStatusPredicate(Integer status){
            this.status = status;
        }
        @Override
        public Predicate getPredicate() {
            return VALID_STATUS.eq(ValidStatus.of(status).orElse(null));
        }
    }

    public static class StoreTypePredicate extends AbstractPredicate{
        private final Long storeTypeId;
        public StoreTypePredicate(Long storeTypeId){
            this.storeTypeId = storeTypeId;
        }
        @Override
        public Predicate getPredicate() {
            return STORE_TYPE.eq(storeTypeId);
        }
    }

}
