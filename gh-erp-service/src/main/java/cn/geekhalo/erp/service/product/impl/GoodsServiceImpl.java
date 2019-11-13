package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.constants.GoodsType;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.model.JsonObject;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.domain.domainservice.IGoodsDomainService;
import cn.geekhalo.erp.domain.product.Goods;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.product.QGoods;
import cn.geekhalo.erp.domain.product.vo.GoodsVo;
import cn.geekhalo.erp.domain.storehouse.Store;
import cn.geekhalo.erp.dto.product.*;
import cn.geekhalo.erp.repository.product.GoodsRepository;
import cn.geekhalo.erp.repository.storehouse.StoreRepository;
import cn.geekhalo.erp.service.product.IGoodsService;
import cn.geekhalo.erp.service.product.IProductSpecificationService;
import cn.geekhalo.erp.util.WaterFlowUtils;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GoodsServiceImpl extends AbstractService implements IGoodsService {

    protected GoodsServiceImpl() {
        super(log);
    }

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private IProductSpecificationService productSpecificationService;

    @Autowired
    private IGoodsDomainService goodsDomainService;

    private static String DEFAULT_PROVIDER = "默认供应商";

    //商品批量入库
    @Override
    public void goodsCreateInStore(GoodsInStoreDto dto) {
        log.info("goodsCreateInStore" + JSON.toJSONString(dto));
        //条码不能重复
        Optional<Store> store = storeRepository.findById(dto.getStoreId());
        Optional<ProductSpecification> specification = productSpecificationService.findById(dto.getProductSpecId());
        if(!specification.isPresent() || !store.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        JsonObject<List<String>> result = goodsDomainService.check(dto.getBarcodes().stream().map(attr -> attr.getBarCode()).collect(Collectors.toList()));
        if(!Objects.equals(result.getCode(),CodeEnum.Success.getCode())){
            throw new BusinessException(ErrorMsg.BarCodeDuplicate,result.getResult());
        }
        //设置为同一个批次
        final Long batchId = WaterFlowUtils.nextWaterFlow();
        dto.getBarcodes().stream().
                map(bar -> new Goods.GoodsCreateDto(bar.getBarCode(),dto.getOperateUser(),dto.getOperateTime(),""))
                .collect(Collectors.toList())
                .stream().forEach(
                goodsValueObject -> creatorFor(goodsRepository)
                        .instance(() -> new Goods())
                        .update(goods -> goods.goodsBasicInStore(goodsValueObject,store.get().getId(),specification.get().getId(),batchId))
                        .call()
        );
    }

    //商品制造有新产品码-->多码合一
    @Override
    public void goodsMake(GoodsMakeDto dto) {
        goodsDomainService.checkGoodsValid(dto.getStoreId(),dto.getChildrenGoodsInfos().stream().map(ci -> ci.getBarCode()).collect(Collectors.toList()));
        log.info("goodsMake" + JSON.toJSONString(dto));
        Optional<Store> store = storeRepository.findById(dto.getStoreId());
        Optional<ProductSpecification> specification = productSpecificationService.findById(dto.getSpecId());
        if(!store.isPresent() || !specification.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        Optional<Goods> go = goodsRepository.findByGoodsTypeAndBarCodeAndStoreId(GoodsType.GOODS_MAKE,dto.getGoodsBarCode(),store.get().getId());
        if(go.isPresent()){
            throw new BusinessException(ErrorMsg.BarCodeDuplicate);
        }

        Long makeOutBatchId = WaterFlowUtils.nextWaterFlow();
        Goods.GoodsCreateDto gcd = new Goods.GoodsCreateDto(dto.getGoodsBarCode(),dto.getOperateUser(),Instant.now().toEpochMilli(),DEFAULT_PROVIDER);
        log.info("生产件入库");
       Goods goods =  creatorFor(goodsRepository)
                .instance(() -> new Goods())
                .update(gd -> gd.goodsMakeInStore(store.get().getId(),specification.get().getId(),gcd,WaterFlowUtils.nextWaterFlow()))
                .call();
        dto.getChildrenGoodsInfos().stream().forEach(child -> {
            Goods outGoods = checkBarcodeIsCorrectAndGet(child.getBarCode(),store.get().getId(),child.getSpecId());
            log.info("生产零部件出库开始");
            updaterFor(goodsRepository)
                    .id(outGoods.getId())
                    .update(g -> g.goodsMakeUsed(makeOutBatchId,dto.getOperateUser()))
                    .update(gd -> gd.parentId(goods.getId()))
                    .call();
            log.info("生产零部件出库结束");
        });


    }

    @Override
    public void goodsAssemble(GoodsAssembleDto dto) {
        goodsDomainService.checkGoodsValid(dto.getStoreId(),dto.getChildrenGoodsInfos().stream().map(ci -> ci.getBarCode()).collect(Collectors.toList()));
        log.info("goodsAssemble" + JSON.toJSONString(dto));
        Optional<Store> store = storeRepository.findById(dto.getStoreId());
        Optional<ProductSpecification> specification = productSpecificationService.findById(dto.getSpecId());
        if(!store.isPresent() || !specification.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        Optional<Goods> go = goodsRepository.findByGoodsTypeAndBarCodeAndStoreId(GoodsType.GOODS_ASSEMBLE,dto.getGoodsBarCode(),store.get().getId());
        if(go.isPresent()){
            throw new BusinessException(ErrorMsg.BarCodeDuplicate);
        }
        Long assembleOutBatchId = WaterFlowUtils.nextWaterFlow();
        log.info("配货件入库");
        Goods.GoodsCreateDto gcd = new Goods.GoodsCreateDto(dto.getGoodsBarCode(),dto.getOperateUser(),Instant.now().toEpochMilli(),DEFAULT_PROVIDER);
        Goods goods = creatorFor(goodsRepository).instance(() -> new Goods())
                .update(gd -> gd.goodsAssembleInStore(store.get().getId(),specification.get().getId(),gcd,WaterFlowUtils.nextWaterFlow()))
                .call();
        dto.getChildrenGoodsInfos().stream().forEach(child -> {
            Goods outGoods = checkBarcodeIsCorrectAndGet(child.getBarCode(),store.get().getId(),child.getSpecId());
            log.info("组装配货零部件出库开始");
            updaterFor(goodsRepository)
                    .id(outGoods.getId())
                    .update(g -> g.goodsAssembleUsed(assembleOutBatchId,dto.getOperateUser()))
                    .update(gd -> gd.parentId(goods.getId()))
                    .call();
            log.info("组装配货零部件出库成功");
        });
    }


    @Override
    public Optional<Goods> findById(Long id) {
        return goodsRepository.findById(id);
    }

    //生成非序列化商品的条码
    @Override
    public List<String> generateBarCode(Integer number) {
        List<String> list = Lists.newArrayList();
        for(int i=0;i<number;i++){
            list.add(IdUtil.simpleUUID());
        }
        return list;
    }

    //找出可用的非序列化商品的条码
    @Override
    public List<String> getAvailableGoodsBars(Long specId, Integer number,Long storeId) {
        Optional<ProductSpecification> specification = productSpecificationService.findById(specId);
        if(!specification.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        List<Goods> goodsList = goodsRepository.findBySpecificationIdAndValidStatusAndStoreId(specification.get().getId(), ValidStatus.VALID, PageRequest.of(0,number),storeId);
        return goodsList.stream().map(goods -> goods.getBarCode()).collect(Collectors.toList());
    }

    @Override
    public List<Goods> findByBars(List<String> bars) {
        Predicate barCode = QGoods.goods.barCode.in(bars);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Iterable<Goods> goods =  goodsRepository.findAll(booleanBuilder.and(barCode));
        List<Goods> list = Lists.newArrayList();
        goods.forEach(g -> list.add(g));
        return list;
    }

    @Override
    public Optional<GoodsVo> findByStoreIdAndBarcode(Long storeId, String barCode) {
        Optional<Goods> goods = goodsRepository.findGoodsByBarCodeAndStoreId(barCode,storeId);
        if(goods.isPresent()){
            return  Optional.of(new GoodsVo(goods.get()));
        }
        return Optional.empty();
    }

//    @Override
//    public void changeGoodsStatusToAssembleUsed(Long id) {
//        updaterFor(goodsRepository)
//                .id(id)
//                .update(gs -> gs.goodsStatusToAssembleUsed())
//                .call();
//    }

    @Override
    public List<GoodsVo> getGoodsChildrenInfo(Long id) {
        return goodsRepository.findByParentId(id).stream().map(goods -> new GoodsVo(goods,productSpecificationService.findById(goods.getSpecificationId()).get().getProductSpecName())).collect(Collectors.toList());
    }

    @Override
    public Page<GoodsVo> findByPage(PageRequestWrapper<GoodsQuery> pageWrapper) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.nonNull(pageWrapper.getBean().getSpecId())){
            booleanBuilder.and(QGoods.goods.specificationId.eq(pageWrapper.getBean().getSpecId()));
        }
        if(Objects.nonNull(pageWrapper.getBean().getGoodsType())){
            booleanBuilder.and(QGoods.goods.goodsType.eq(GoodsType.of(pageWrapper.getBean().getGoodsType()).orElse(GoodsType.GOODS_BASIC)));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStatus())){
            booleanBuilder.and(QGoods.goods.validStatus.eq(ValidStatus.of(pageWrapper.getBean().getStatus()).orElse(ValidStatus.VALID)));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStoreId())){
            booleanBuilder.and(QGoods.goods.storeId.eq(pageWrapper.getBean().getStoreId()));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStartTime())){
            booleanBuilder.and(QGoods.goods.createdAt.after(Instant.ofEpochMilli(pageWrapper.getBean().getStartTime())));
        }
        if(Objects.nonNull(pageWrapper.getBean().getEndTime())){
            booleanBuilder.and(QGoods.goods.createdAt.before(Instant.ofEpochMilli(pageWrapper.getBean().getEndTime())));
        }
        Page<Goods> page = goodsRepository.findAll(booleanBuilder,PageRequest.of(pageWrapper.getPage()-1,pageWrapper.getSize()));
        return new PageImpl<>(page.getContent()
                .stream()
                .map(goods -> new GoodsVo(goods,productSpecificationService.findById(goods.getSpecificationId()).get().getProductSpecName(),storeRepository.findById(goods.getStoreId()).get().getStoreHouseName()))
                .collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
    }

    @Override
    public void goodSaleToAgent(Long batchId, Long storeId, String barCode,String operateUser) {
        Optional<Goods> goods = goodsRepository.findGoodsByBarCodeAndStoreIdAndValidStatus(barCode,storeId,ValidStatus.VALID);
        if(goods.isPresent()){
            updaterFor(goodsRepository)
                    .id(goods.get().getId())
                    .update(gd -> gd.goodsSaleToAgent(batchId,operateUser))
                    .call();
        }else {
            log.info("batId:{},storeId:{},barCode:{} 该商品无效",batchId,storeId,barCode);
        }

    }

    @Override
    public void goodSaleToUser(Long batchId, Long storeId, String barCode, String operateUser) {
        Optional<Goods> goods = goodsRepository.findGoodsByBarCodeAndStoreIdAndValidStatus(barCode,storeId,ValidStatus.VALID);
        if(goods.isPresent()){
            updaterFor(goodsRepository)
                    .id(goods.get().getId())
                    .update(gd -> gd.goodSaleToUser(batchId,operateUser))
                    .call();
        }else {
            log.info("batId:{},storeId:{},barCode:{} 该商品无效",batchId,storeId,barCode);
        }
    }

    @Override
    public void goodsTransferOutToOtherStore(Long batchId, Long storeId, String barCode, String operateUser) {
        Optional<Goods> goods = goodsRepository.findGoodsByBarCodeAndStoreIdAndValidStatus(barCode,storeId,ValidStatus.VALID);
        if(goods.isPresent()){
            updaterFor(goodsRepository)
                    .id(goods.get().getId())
                    .update(gd -> gd.goodsTransferToOtherStore(batchId,operateUser))
                    .call();
        }else {
            log.info("batId:{},storeId:{},barCode:{} 该商品无效",batchId,storeId,barCode);
        }
    }

    @Override
    public void goodsTransferInStore(Long batchId, Long inStoreId,Long outStoreId, String barCode, String operateUser) {
        Optional<Goods> outGoods = goodsRepository.findGoodsByBarCodeAndStoreId(barCode,outStoreId);
        Goods.GoodsCreateDto goodsCreateDto = new Goods.GoodsCreateDto(barCode,operateUser,Instant.now().toEpochMilli(),outGoods.get().getProviderName());
        creatorFor(goodsRepository)
                .instance(() -> new Goods())
                .update(goods -> goods.goodsTransferInStore(goodsCreateDto,inStoreId,outGoods.get().getSpecificationId(),batchId,outGoods.get().getGoodsType()));

    }

    @Override
    public void goodsBuyInStore(Long batchId, Long inStoreId, Long outStoreId, String barCode, String operateUser) {
        Optional<Goods> outGoods = goodsRepository.findGoodsByBarCodeAndStoreId(barCode,outStoreId);
        Goods.GoodsCreateDto goodsCreateDto = new Goods.GoodsCreateDto(barCode,operateUser,Instant.now().toEpochMilli(),outGoods.get().getProviderName());
        creatorFor(goodsRepository)
                .instance(() -> new Goods())
                .update(goods -> goods.goodsBuyInStore(goodsCreateDto,inStoreId,outGoods.get().getSpecificationId(),batchId,outGoods.get().getGoodsType()));
    }

    //检查商品录入的编码是否符合规格的约束
    private Goods checkBarcodeIsCorrectAndGet(String barcode,Long storeId,Long specId){
        Optional<Goods> goods = goodsRepository.findGoodsByBarCodeAndStoreId(barcode,storeId);
        if(!goods.isPresent()){
            throw new BusinessException(ErrorMsg.GoodsNotExist);
        }else {
           if(!Objects.equals(ValidStatus.VALID.getCode(),goods.get().getValidStatus().getCode())){
               throw new BusinessException(ErrorMsg.GoodsNotAvailable);
           }
           if(goods.get().getSpecificationId().longValue() != specId.longValue()){
               throw new BusinessException(ErrorMsg.SpecIsNotCorrect);
           }
        }
        return goods.get();
    }
}
