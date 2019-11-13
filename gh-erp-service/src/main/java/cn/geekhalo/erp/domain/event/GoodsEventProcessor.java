package cn.geekhalo.erp.domain.event;

import cn.geekhalo.erp.constants.InOutDirectionType;
import cn.geekhalo.erp.constants.InOutOperationType;
import cn.geekhalo.erp.constants.TraceType;
import cn.geekhalo.erp.domain.product.Goods;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.product.creator.GoodsTraceLogCreator;
import cn.geekhalo.erp.domain.storehouse.StoreSpecificationSummary;
import cn.geekhalo.erp.dto.product.InitSpecificationDto;
import cn.geekhalo.erp.dto.storehouse.CreateStoreRecordDto;
import cn.geekhalo.erp.service.product.IGoodsService;
import cn.geekhalo.erp.service.product.IGoodsTraceLogService;
import cn.geekhalo.erp.service.product.IProductSpecificationService;
import cn.geekhalo.erp.service.storehouse.IStoreInOutRecordService;
import cn.geekhalo.erp.service.storehouse.IStoreSpecificationSummaryService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GoodsEventProcessor {

    @Autowired
    private IStoreInOutRecordService storeInOutRecordService;

    @Autowired
    private IGoodsTraceLogService goodsTraceLogService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IStoreSpecificationSummaryService storeSpecificationSummaryService;

    @Autowired
    private IProductSpecificationService specificationService;

    //handle GoodsCreateEvent -->分别监听这种方式适合后期快速切换到mq ===================================
    //零部件入库
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleGoodsCreateEventForRecord(GoodsEvents.GoodsInEvent event){
        Goods goods = goodsService.findById(event.getGoods().getId()).get();
        ProductSpecification specification = specificationService.findById(goods.getSpecificationId()).get();
        CreateStoreRecordDto dto = CreateStoreRecordDto.builder()
                .batchId(goods.getBatchId())
                .directionType(InOutDirectionType.IN)
                .operationType(event.getInOutOperationType())
                .goodsId(goods.getId())
                .operateUser(goods.getOperateUser())
                .operateTime(event.getOperateTime())
                .providerName(goods.getProviderName())
                .price(specification.getPrice())
                .remark(event.getInOutOperationType().name())
                .build();
        storeInOutRecordService.addRecord(dto,goods.getSpecificationId(),goods.getStoreId());
    }
    //产品录入库后，保存入库日志
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleGoodsCreateEventForLog(GoodsEvents.GoodsInEvent event){
        Goods goods = goodsService.findById(event.getGoods().getId()).get();
        Map<String,Object> map = Maps.newHashMap();
        map.put("provider",event.getProviderName());
        handleGoodsLog(goods, TraceType.IN_STORE,event.getInOutOperationType(),map);

    }

    //产品录入库后，添加库存信息
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleGoodsCreateEventForSpecification(GoodsEvents.GoodsInEvent event){
        Goods goods = goodsService.findById(event.getGoods().getId()).get();
        handleSpecificationAdd(goods);
    }

    //handle GoodOutEvent ==========================================================================

    //商品被使用出库更新规格的数量
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleGoodsOutEventForRecord(GoodsEvents.GoodsOutEvent event){
        Goods goods = goodsService.findById(event.getGoods().getId()).get();
        CreateStoreRecordDto dto = CreateStoreRecordDto.builder()
                .batchId(goods.getBatchId())
                .directionType(InOutDirectionType.OUT)
                .operationType(event.getInOutOperationType())
                .goodsId(goods.getId())
                .remark(event.getInOutOperationType().name())
                .build();
        //产品被组装使用后添加库存明细
        storeInOutRecordService.addRecord(dto,goods.getSpecificationId(),goods.getStoreId());
        Map<String,Object> map = Maps.newHashMap();
        handleGoodsLog(goods,TraceType.OUT_STORE,event.getInOutOperationType(),map);
        handleSpecificationSub(goods);
    }
    //公用方法模块++++++++++++++++++++++++++++++++++++
    private void handleSpecificationAdd(Goods goods){
        Optional<ProductSpecification> specification = specificationService.findById(goods.getSpecificationId());
        InitSpecificationDto dto = InitSpecificationDto.builder()
                .specificationId(goods.getSpecificationId())
                .specificationName(specification.get().getProductSpecName())
                .stock(1)
                .totalPrice(specification.get().getPrice())
                .storeId(goods.getStoreId())
                .build();
        Optional<StoreSpecificationSummary> summary = storeSpecificationSummaryService.findByStoreAndSpecificationId(goods.getStoreId(),goods.getSpecificationId());
        if(summary.isPresent()){
            storeSpecificationSummaryService.addStockAndPrice(summary.get().getId(),1,specification.get().getPrice());
        }else {
            storeSpecificationSummaryService.createStoreSpecificationSummary(dto);
        }
    }

    private void handleSpecificationSub(Goods goods){
        Optional<StoreSpecificationSummary> summary = storeSpecificationSummaryService.findByStoreAndSpecificationId(goods.getStoreId(),goods.getSpecificationId());
        Optional<ProductSpecification> specification = specificationService.findById(goods.getSpecificationId());
        storeSpecificationSummaryService.subStockAndPrice(summary.get().getId(),1,specification.get().getPrice());
    }

    private void handleGoodsLog(Goods goods, TraceType traceType, InOutOperationType type, Map<String, Object> map){
        GoodsTraceLogCreator creator = new GoodsTraceLogCreator();
        creator.barcode(goods.getBarCode());
        creator.flowWater(goods.getBatchId());
        creator.operationType(type);
        creator.goodsId(goods.getId());
        creator.storeId(goods.getStoreId());
        creator.logDesc(JSON.toJSONString(map));
        creator.traceType(traceType);
        goodsTraceLogService.createTraceLog(creator);
}


}
