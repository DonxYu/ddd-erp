package cn.geekhalo.erp.service.product;

import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.erp.domain.product.Goods;
import cn.geekhalo.erp.domain.product.vo.GoodsVo;
import cn.geekhalo.erp.dto.product.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IGoodsService {
    //商品入库
    void goodsCreateInStore(GoodsInStoreDto dto);
    //保留某个商品的编码--进行组装
    //商品生产
    void goodsMake(GoodsMakeDto dto);
    //商品组装配货
    void goodsAssemble(GoodsAssembleDto dto);

    Optional<Goods> findById(Long id);

    List<String> generateBarCode(Integer number);

    List<String> getAvailableGoodsBars(Long specId, Integer number, Long storeId);

    List<Goods> findByBars(List<String> bars);

    Optional<GoodsVo> findByStoreIdAndBarcode(Long storeId, String barCode);

    List<GoodsVo> getGoodsChildrenInfo(Long id);

    Page<GoodsVo> findByPage(PageRequestWrapper<GoodsQuery> pageWrapper);

    void goodSaleToAgent(Long batchId, Long storeId, String barCode, String operateUser);

    void goodSaleToUser(Long batchId, Long storeId, String barCode, String operateUser);

    void goodsTransferOutToOtherStore(Long batchId, Long storeId, String barCode, String operateUser);

    void goodsTransferInStore(Long batchId, Long inStoreId, Long outStoreId, String barCode, String operateUser);

    void goodsBuyInStore(Long batchId, Long inStoreId, Long outStoreId, String barCode, String operateUser);

}
