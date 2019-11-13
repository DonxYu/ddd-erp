package cn.geekhalo.erp.service.storehouse.impl;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.qsl.PredicateAware;
import cn.geekhalo.common.qsl.PredicateUtils;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.constants.StoreCategory;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.product.creator.StoreCreator;
import cn.geekhalo.erp.domain.storehouse.*;
import cn.geekhalo.erp.dto.storehouse.StoreCreateDto;
import cn.geekhalo.erp.dto.storehouse.StoreQueryReq;
import cn.geekhalo.erp.repository.storehouse.StoreRepository;
import cn.geekhalo.erp.repository.storehouse.StoreSpecRestrictRepository;
import cn.geekhalo.erp.repository.storehouse.StoreTypeRepository;
import cn.geekhalo.erp.service.predicate.StorePredicate;
import cn.geekhalo.erp.service.product.IProductSpecificationService;
import cn.geekhalo.erp.service.storehouse.IStoreService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StoreServiceImpl extends AbstractService implements IStoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    protected StoreServiceImpl() {
        super(logger);
    }

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreSpecRestrictRepository specRestrictRepository;

    @Autowired
    private IProductSpecificationService productSpecificationService;

    @Autowired
    private StoreTypeRepository storeTypeRepository;

    @Override
    public Store createStore(StoreCreateDto dto) {
        StoreCreator creator = new StoreCreator();
        creator.contactPhone(dto.getContactPhone());
        creator.contactUsername(dto.getContactUsername());
        creator.address(dto.getAddress());
        creator.storeHouseName(dto.getStoreName());
        creator.storeTypeId(dto.getStoreTypeId());
        creator.storeCode(dto.getStoreCode());
        creator.storeCategory(StoreCategory.STORE_COMPANY_PRODUCT);
        List<Store> list = storeRepository.findByStoreHouseName(dto.getStoreName());
        if(CollectionUtils.isNotEmpty(list)){
            throw new BusinessException(ErrorMsg.StoreNameIsExist);
        }
        Store createStore =  creatorFor(storeRepository)
                .instance(() -> new Store())
                .update(store -> store.init())
                .update(store -> store.doCreateStore(creator))
                .call();
        logger.info("创建仓库成功");
        dto.getSpecIds().stream().forEach(
                specId -> {
                    Optional<ProductSpecification> specification = productSpecificationService.findById(specId);
                    if(specification.isPresent()){
                        creatorFor(specRestrictRepository)
                                .instance(() -> new StoreSpecRestrict(createStore.getId(),specification.get().getId(),specification.get().getProductSpecName()))
                                .call();
                    }
                }
        );
        logger.info("添加仓库约束成功");
        logger.info("添加仓库管理员");
        return createStore;

    }

    @Override
    public Store createSimpleStore(StoreCreator creator) {
        return creatorFor(storeRepository)
                .instance(() -> new Store())
                .update(store -> store.doCreateStore(creator))
                .update(store -> store.init())
                .call();
    }


    @Override
    public void validStore(Long id) {
        updaterFor(storeRepository)
                .id(id)
                .update(store -> store.valid())
                .call();
    }

    @Override
    public void invalidStore(Long id) {
        updaterFor(storeRepository)
                .id(id)
                .update(store -> store.invalid())
                .call();
    }

    @Override
    public List<Store> findAllStore() {
        Iterable<Store> iterable = storeRepository.findAll(QStore.store.validStatus.eq(ValidStatus.VALID).and(QStore.store.storeCategory.eq(StoreCategory.STORE_COMPANY_PRODUCT)));
        List<Store> list = Lists.newArrayList();
        iterable.forEach(
                it -> list.add(it)
        );
        return list;
    }


    @Override
    public Page<StoreVo> findByPage(PageRequestWrapper<StoreQueryReq> req) {
        List<PredicateAware> list = Lists.newArrayList();
        if(!StringUtils.isEmpty(req.getBean().getContactPhone())){
            list.add(new StorePredicate.ContactPhonePredicate(req.getBean().getContactPhone()));
        }
        if(!StringUtils.isEmpty(req.getBean().getContactUsername())){
            list.add(new StorePredicate.ContactUsernamePredicate(req.getBean().getContactUsername()));
        }
        if(!StringUtils.isEmpty(req.getBean().getStoreName())){
            list.add(new StorePredicate.StoreNamePredicate(req.getBean().getStoreName()));
        }
        if(Objects.nonNull(req.getBean().getStoreTypeId())){
            list.add(new StorePredicate.StoreTypePredicate(req.getBean().getStoreTypeId()));
        }
        if(Objects.nonNull(req.getBean().getValidStatus())){
            list.add(new StorePredicate.StoreStatusPredicate(req.getBean().getValidStatus()));
        }
        list.add(new StorePredicate.StoreCategoryPredicate(StoreCategory.STORE_COMPANY_PRODUCT.getCode()));
        Page<Store> page = storeRepository.findAll(PredicateUtils.buildListAndPredicate(list), PageRequest.of(req.getPage()-1,req.getSize()));
        return new PageImpl<>(page.getContent().stream()
                .map(
                    store -> {
                        StoreVo storeVo =   new StoreVo(store);
                        Optional<StoreType> storeType = storeTypeRepository.findById(store.getStoreTypeId());
                        if(storeType.isPresent()){
                            storeVo.setStoreTypeName(storeType.get().getName());
                        }
                        return storeVo;
                    }
                )
                .collect(Collectors.toList())
                ,page.getPageable(),page.getTotalElements());
    }


}
