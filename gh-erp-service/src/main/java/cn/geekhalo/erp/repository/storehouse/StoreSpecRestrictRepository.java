package cn.geekhalo.erp.repository.storehouse;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.storehouse.StoreSpecRestrict;

import java.util.List;

public interface StoreSpecRestrictRepository extends BaseRepository<StoreSpecRestrict,Long> {
    List<StoreSpecRestrict> findByStoreId(Long storeId);
}
