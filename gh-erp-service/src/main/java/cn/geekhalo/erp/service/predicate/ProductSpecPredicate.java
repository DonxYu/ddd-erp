package cn.geekhalo.erp.service.predicate;

import cn.geekhalo.common.qsl.AbstractPredicate;
import cn.geekhalo.erp.domain.product.QProductSpecification;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

public class ProductSpecPredicate {
    private static final QProductSpecification Q_PRODUCT_SPECIFICATION = QProductSpecification.productSpecification;
    private static final StringPath PRODUCT_SPEC_CODE = Q_PRODUCT_SPECIFICATION.specCode;
    private static final StringPath PRODUCT_SPEC_NAME = Q_PRODUCT_SPECIFICATION.productSpecName;

    public static class SpecNamePredicate extends AbstractPredicate {
        private final String productSpecName;
        public SpecNamePredicate(String  productSpecName){
            this.productSpecName = productSpecName;
        }
        @Override
        public Predicate getPredicate() {
            return PRODUCT_SPEC_NAME.like("%"+productSpecName+"%");
        }
    }

    public static class ProductSpecCodePredicate extends AbstractPredicate {
        private final String productSpecCode;
        public ProductSpecCodePredicate(String  productSpecCode){
            this.productSpecCode = productSpecCode;
        }
        @Override
        public Predicate getPredicate() {
            return PRODUCT_SPEC_CODE.like("%"+productSpecCode+"%");
        }
    }
}
