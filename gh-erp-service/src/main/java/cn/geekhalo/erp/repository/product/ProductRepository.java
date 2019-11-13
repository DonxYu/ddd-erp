package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.Product;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends BaseRepository<Product,Long>, QuerydslPredicateExecutor<Product> {
}
