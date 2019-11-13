package cn.geekhalo.erp.service.storehouse;

import cn.geekhalo.erp.domain.storehouse.vo.StoreTypeVo;
import cn.geekhalo.erp.dto.storehouse.StoreTypeCreateDto;

import java.util.List;

public interface IStoreTypeService {
    List<StoreTypeVo> findAll();
    void createStoreType(StoreTypeCreateDto dto);
    void invalid(Long id);
    void valid(Long id);
    List<StoreTypeVo> findAllStoreTypeList();
}
