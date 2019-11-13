package cn.geekhalo.erp.service.storehouse.impl;

import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.storehouse.StoreSpecRestrict;
import cn.geekhalo.erp.repository.storehouse.StoreSpecRestrictRepository;
import cn.geekhalo.erp.service.storehouse.IStoreSpecRestrictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class StoreSpecRestrictServiceImpl extends AbstractService implements IStoreSpecRestrictService {

    protected StoreSpecRestrictServiceImpl() {
        super(log);
    }

    @Autowired
    private StoreSpecRestrictRepository storeSpecRestrictRepository;

    @Override
    public void createStoreSpecRestrict(Long storeId, Long specId, String specName) {
        creatorFor(storeSpecRestrictRepository)
                .instance(() -> new StoreSpecRestrict(storeId,specId,specName))
                .call();
    }

    @Override
    public List<StoreSpecRestrict> findByStoreId(Long storeId) {
        return storeSpecRestrictRepository.findByStoreId(storeId);
    }
}
