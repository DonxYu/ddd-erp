package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.ProductCategory;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory,Long>, QuerydslPredicateExecutor<ProductCategory> {
}
