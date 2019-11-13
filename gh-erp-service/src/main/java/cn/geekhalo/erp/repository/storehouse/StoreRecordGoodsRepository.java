package cn.geekhalo.erp.repository.storehouse;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.storehouse.StoreRecordGoods;

import java.util.List;

public interface StoreRecordGoodsRepository extends BaseRepository<StoreRecordGoods,Long> {
    List<StoreRecordGoods> findByStoreInOutId(Long recordId);
}
