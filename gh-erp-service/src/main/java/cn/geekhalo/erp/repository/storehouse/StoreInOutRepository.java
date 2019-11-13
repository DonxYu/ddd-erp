package cn.geekhalo.erp.repository.storehouse;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.storehouse.StoreInOutRecord;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface StoreInOutRepository extends BaseRepository<StoreInOutRecord,Long>, QuerydslPredicateExecutor<StoreInOutRecord> {
    Optional<StoreInOutRecord> findByGoodsBatchIdAndStoreId(Long batchId, Long storeId);
}
