package cn.geekhalo.erp.repository.storehouse;

import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.storehouse.StoreSpecificationSummary;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface StoreSpecificationSummaryRepository extends BaseRepository<StoreSpecificationSummary,Long>, QuerydslPredicateExecutor<StoreSpecificationSummary> {
    Optional<StoreSpecificationSummary> findByStoreIdAndSpecificationId(Long storeId, Long specId);
}
