package cn.geekhalo.erp.repository.product;

import cn.geekhalo.common.constants.GoodsType;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.Goods;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface GoodsRepository extends BaseRepository<Goods,Long>, QuerydslPredicateExecutor<Goods> {
    List<Goods> findGoodsByBarCodeIn(List<String> bars);
    Optional<Goods> findGoodsByBarCodeAndStoreId(String barCode, Long storeId);
    Optional<Goods> findGoodsByBarCodeAndStoreIdAndValidStatus(String barCode, Long storeId, ValidStatus validStatus);
    Optional<Goods> findByGoodsTypeAndBarCodeAndStoreId(GoodsType type, String barCode, Long storeId);
    List<Goods> findBySpecificationIdAndValidStatusAndStoreId(Long specificationId, ValidStatus status, Pageable pageable, Long storeId);
    List<Goods> findBySpecificationIdAndValidStatusAndStoreId(Long specificationId, ValidStatus status, Long storeId);
    List<Goods> findByParentId(Long pid);
    List<Goods> findByStoreIdAndValidStatus(Long storeId, ValidStatus validStatus);
}
