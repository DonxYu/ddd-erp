package cn.geekhalo.erp.service.storehouse;

import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.erp.domain.storehouse.vo.StoreRecordVo;
import cn.geekhalo.erp.dto.storehouse.CreateStoreRecordDto;
import cn.geekhalo.erp.dto.storehouse.StoreRecordQuery;
import org.springframework.data.domain.Page;


public interface IStoreInOutRecordService {
    void addRecord(CreateStoreRecordDto dto, Long specificationId, Long storeId);
    Page<StoreRecordVo> findByPage(PageRequestWrapper<StoreRecordQuery> pageRequestWrapper);
    Page<StoreRecordVo> findInOutStoreRecordByPage(PageRequestWrapper<StoreRecordQuery> pageRequestWrapper);
}
