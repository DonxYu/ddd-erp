package cn.geekhalo.erp.repository.product;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductSpecificationRepository extends BaseRepository<ProductSpecification,Long>, QuerydslPredicateExecutor<ProductSpecification> {
    Optional<ProductSpecification> findBySpecCode(String specCode);
    List<ProductSpecification> findByOnlineStatusAndValidStatus(ValidStatus online, ValidStatus status);
    Optional<ProductSpecification> findByProductSpecName(String name);
}
