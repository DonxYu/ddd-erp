package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.ProductType;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductTypeRepository extends BaseRepository<ProductType,Long>, QuerydslPredicateExecutor<ProductType> {
}
