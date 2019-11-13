package cn.geekhalo.erp.service.storehouse;


import cn.geekhalo.erp.domain.storehouse.StoreSpecRestrict;

import java.util.List;

public interface IStoreSpecRestrictService {

    void createStoreSpecRestrict(Long storeId, Long specId, String specName);

    List<StoreSpecRestrict> findByStoreId(Long storeId);
}
