package cn.geekhalo.erp.service.storehouse.impl;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.constants.InOutDirectionType;
import cn.geekhalo.erp.domain.product.Goods;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.storehouse.QStoreInOutRecord;
import cn.geekhalo.erp.domain.storehouse.Store;
import cn.geekhalo.erp.domain.storehouse.StoreInOutRecord;
import cn.geekhalo.erp.domain.storehouse.StoreRecordGoods;
import cn.geekhalo.erp.domain.storehouse.vo.StoreRecordVo;
import cn.geekhalo.erp.dto.storehouse.CreateStoreRecordDto;
import cn.geekhalo.erp.dto.storehouse.StoreRecordQuery;
import cn.geekhalo.erp.repository.product.GoodsRepository;
import cn.geekhalo.erp.repository.product.ProductSpecificationRepository;
import cn.geekhalo.erp.repository.storehouse.StoreInOutRepository;
import cn.geekhalo.erp.repository.storehouse.StoreRecordGoodsRepository;
import cn.geekhalo.erp.repository.storehouse.StoreRepository;
import cn.geekhalo.erp.service.storehouse.IStoreInOutRecordService;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class StoreInOutRecordServiceImpl extends AbstractService implements IStoreInOutRecordService {

    protected StoreInOutRecordServiceImpl() {
        super(log);
    }

    @Autowired
    private StoreInOutRepository storeInOutRepository;

    @Autowired
    private ProductSpecificationRepository specificationRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StoreRecordGoodsRepository storeRecordGoodsRepository;

    @Override
    @Transactional(rollbackOn = BusinessException.class)
    public void addRecord(CreateStoreRecordDto dto, Long specificationId, Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        Optional<ProductSpecification> specification = specificationRepository.findById(specificationId);
        if(!specification.isPresent() || !store.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        Optional<StoreInOutRecord> storeInOutRecord = storeInOutRepository.findByGoodsBatchIdAndStoreId(dto.getBatchId(),store.get().getId());
        Optional<Goods> goods = goodsRepository.findById(dto.getGoodsId());
        if(!storeInOutRecord.isPresent()){
            StoreInOutRecord  sior = creatorFor(storeInOutRepository)
                    .instance(() -> new StoreInOutRecord())
                    .update(record -> record.addRecord(dto,specificationId,storeId))
                    .call();
            log.info("添加出入库记录成功");
            log.info("添加产品明细开始");
            creatorFor(storeRecordGoodsRepository)
                    .instance(() -> new StoreRecordGoods(sior.getId(),goods.get().getId(),specification.get().getProductSpecName(),goods.get().getBarCode()))
                    .call();
            log.info("添加产品明细结束");
        }else {
            updaterFor(storeInOutRepository)
                    .loader(() -> storeInOutRecord)
                    .update(record -> record.updatePriceAndCount(specification.get().getPrice(),1))
                    .call();
            log.info("添加产品明细开始");
            creatorFor(storeRecordGoodsRepository)
                    .instance(() -> new StoreRecordGoods(storeInOutRecord.get().getId(),goods.get().getId(),specification.get().getProductSpecName(),goods.get().getBarCode()))
                    .call();
            log.info("添加产品明细结束");
        }
    }

    @Override
    public Page<StoreRecordVo> findByPage(PageRequestWrapper<StoreRecordQuery> pageWrapper) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.nonNull(pageWrapper.getBean().getSpecId())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.specificationId.eq(pageWrapper.getBean().getSpecId()));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStoreId())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.storeId.eq(pageWrapper.getBean().getStoreId()));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStartTime())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.createdAt.after(Instant.ofEpochMilli(pageWrapper.getBean().getStartTime())));
        }
        if(Objects.nonNull(pageWrapper.getBean().getEndTime())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.createdAt.before(Instant.ofEpochMilli(pageWrapper.getBean().getEndTime())));
        }
        booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.directionType.eq(InOutDirectionType.IN));
        Page<StoreInOutRecord> page = storeInOutRepository.findAll(booleanBuilder,PageRequest.of(pageWrapper.getPage()-1,pageWrapper.getSize()));
        return new PageImpl<>(page.getContent().stream()
                .map(record -> new StoreRecordVo(record,storeRepository.findById(record.getStoreId()).get().getStoreHouseName(),specificationRepository.findById(record.getSpecificationId()).get().getProductSpecName())).collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
    }

    @Override
    public Page<StoreRecordVo> findInOutStoreRecordByPage(PageRequestWrapper<StoreRecordQuery> pageWrapper) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.nonNull(pageWrapper.getBean().getSpecId())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.specificationId.eq(pageWrapper.getBean().getSpecId()));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStoreId())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.storeId.eq(pageWrapper.getBean().getStoreId()));
        }
        if(Objects.nonNull(pageWrapper.getBean().getStartTime())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.createdAt.after(Instant.ofEpochMilli(pageWrapper.getBean().getStartTime())));
        }
        if(Objects.nonNull(pageWrapper.getBean().getEndTime())){
            booleanBuilder.and(QStoreInOutRecord.storeInOutRecord.createdAt.before(Instant.ofEpochMilli(pageWrapper.getBean().getEndTime())));
        }
        Page<StoreInOutRecord> page = storeInOutRepository.findAll(booleanBuilder,PageRequest.of(pageWrapper.getPage()-1,pageWrapper.getSize()));
        return new PageImpl<>(page.getContent().stream()
                .map(record -> new StoreRecordVo(record,storeRepository.findById(record.getStoreId()).get().getStoreHouseName(),specificationRepository.findById(record.getSpecificationId()).get().getProductSpecName())).collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
    }


}
