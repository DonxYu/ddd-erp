package cn.geekhalo.erp.repository.storehouse;

import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.storehouse.Store;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface StoreRepository extends BaseRepository<Store,Long>, QuerydslPredicateExecutor<Store> {
    List<Store> findByStoreHouseName(String storeName);
}
