package cn.geekhalo.erp.service.predicate;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.qsl.AbstractPredicate;
import cn.geekhalo.erp.domain.product.QProduct;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class ProductPredicate {
    private static final QProduct Q_PRODUCT = QProduct.product;
    private static final StringPath PRODUCT_NAME = Q_PRODUCT.productName;
    private static final EnumPath VALID_STATUS = Q_PRODUCT.validStatus;
    private static final NumberPath PRODUCT_TYPE_ID = QProduct.product.productTypeId;
    private static final NumberPath CATEGORY_ID = QProduct.product.categoryId;

    public static class ProductNamePredicate extends AbstractPredicate {
        private final String productName;
        public ProductNamePredicate(String  productName){
            this.productName = productName;
        }
        @Override
        public Predicate getPredicate() {
            return PRODUCT_NAME.like("%"+productName+"%");
        }
    }

    public static class ProductStatusPredicate extends AbstractPredicate{
        private final Integer status;
        public ProductStatusPredicate(Integer status){
            this.status = status;
        }
        @Override
        public Predicate getPredicate() {
            return VALID_STATUS.eq(ValidStatus.of(status).orElse(null));
        }
    }

    public static class ProductCategoryPredicate extends AbstractPredicate{
        private final Long categoryId;
        public ProductCategoryPredicate(Long categoryId){
            this.categoryId = categoryId;
        }
        @Override
        public Predicate getPredicate() {
            return CATEGORY_ID.eq(categoryId.longValue());
        }
    }

    public static class ProductTypePredicate extends AbstractPredicate{
        private final Long typeId;
        public ProductTypePredicate(Long typeId){
            this.typeId = typeId;
        }
        @Override
        public Predicate getPredicate() {
            return PRODUCT_TYPE_ID.eq(typeId.longValue());
        }
    }

}
