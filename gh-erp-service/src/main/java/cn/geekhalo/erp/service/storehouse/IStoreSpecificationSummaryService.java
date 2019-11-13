package cn.geekhalo.erp.service.storehouse;

import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.erp.domain.storehouse.StoreSpecificationSummary;
import cn.geekhalo.erp.domain.storehouse.vo.StoreSpecificationSummaryVo;
import cn.geekhalo.erp.dto.product.InitSpecificationDto;
import cn.geekhalo.erp.dto.storehouse.StoreSummaryQuery;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.Optional;

public interface IStoreSpecificationSummaryService {
    void createStoreSpecificationSummary(InitSpecificationDto dto);
    void addStockAndPrice(Long summaryId, Integer stock, BigDecimal price);
    void subStockAndPrice(Long summaryId, Integer stock, BigDecimal price);
    Optional<StoreSpecificationSummary> findByStoreAndSpecificationId(Long storeId, Long specId);
    Page<StoreSpecificationSummaryVo> findByPage(PageRequestWrapper<StoreSummaryQuery> pageRequestWrapper);
}
