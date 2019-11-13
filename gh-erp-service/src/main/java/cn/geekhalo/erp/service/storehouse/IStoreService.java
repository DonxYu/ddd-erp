package cn.geekhalo.erp.service.storehouse;

import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.erp.domain.product.creator.StoreCreator;
import cn.geekhalo.erp.domain.storehouse.Store;
import cn.geekhalo.erp.domain.storehouse.StoreVo;
import cn.geekhalo.erp.dto.storehouse.StoreCreateDto;
import cn.geekhalo.erp.dto.storehouse.StoreQueryReq;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IStoreService {
    Store createStore(StoreCreateDto dto);

    Store createSimpleStore(StoreCreator creator);

    void validStore(Long id);

    void invalidStore(Long id);

    List<Store> findAllStore();

    Page<StoreVo> findByPage(PageRequestWrapper<StoreQueryReq> req);
}
