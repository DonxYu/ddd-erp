package cn.geekhalo.erp.repository.storehouse;

import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.storehouse.StoreType;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StoreTypeRepository extends BaseRepository<StoreType,Long>, QuerydslPredicateExecutor {
}
