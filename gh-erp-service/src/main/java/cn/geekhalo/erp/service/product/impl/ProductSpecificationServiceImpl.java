package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.constants.FlagBit;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.qsl.PredicateAware;
import cn.geekhalo.common.qsl.PredicateUtils;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.domain.product.*;
import cn.geekhalo.erp.domain.product.vo.ProductSpecificationVo;
import cn.geekhalo.erp.domain.product.vo.ProductVo;
import cn.geekhalo.erp.domain.product.vo.SpecDictVo;
import cn.geekhalo.erp.domain.storehouse.StoreSpecRestrict;
import cn.geekhalo.erp.dto.product.*;
import cn.geekhalo.erp.repository.product.*;
import cn.geekhalo.erp.repository.storehouse.StoreSpecRestrictRepository;
import cn.geekhalo.erp.service.predicate.ProductSpecPredicate;
import cn.geekhalo.erp.service.product.IProductSpecificationService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ProductSpecificationServiceImpl extends AbstractService implements IProductSpecificationService {
    protected ProductSpecificationServiceImpl() {
        super(log);
    }

    @Autowired
    private ProductSpecificationRepository specificationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpecificationAttrRepository attrRepository;

    @Autowired
    private ProductSpecificationAttachRepository specificationAttachRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductAttributeRepository attributeRepository;

    @Autowired
    private SpecificationAttrRepository specAttrRepository;

    @Autowired
    private StoreSpecRestrictRepository restrictRepository;

    @Override
    public Optional<ProductSpecification> findById(Long id) {
        return specificationRepository.findById(id);
    }

    @Override
    public Optional<ProductSpecification> findBySpecCode(String specCode) {
        return specificationRepository.findBySpecCode(specCode);
    }


    //添加规格
    @Override
    public ProductSpecification initSpecification(ProductSpecificationInitDto dto) {
        Optional<ProductSpecification> specification = specificationRepository.findBySpecCode(dto.getSpecCode());
        if(specification.isPresent()){
            throw new BusinessException(ErrorMsg.SpecCodeIsExist);
        }
        Optional<Product> product = productRepository.findById(dto.getProductId());
        if(!product.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        ProductSpecification createSpec =  creatorFor(specificationRepository)
                .instance(() -> new ProductSpecification())
                .update(spec -> spec.doCreate(dto))
                .call();
        log.info("创建规格结束");
        dto.getAttrs().forEach(attrWrapper -> {
            attrWrapper.getAttrList().stream().forEach(attrValues -> {
                creatorFor(attrRepository)
                        .instance(() -> new SpecificationAttr())
                        .update(atr -> atr.create(createSpec.getId(),attrWrapper.getAttrId(),attrValues.getAttrValueId(),attrWrapper.getAttrName(),attrValues.getAttrValue(),attrWrapper.getAttrCode()))
                        .call();
            });

        });
        log.info("创建规格值结束");
        return createSpec;
    }

    @Override
    public void assembleSpecification(AssembleSpecDto dto) {
        Optional<ProductSpecification> specification = specificationRepository.findById(dto.getParentId());
        if(!specification.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        dto.getAttaches().forEach(specAttachInfo -> {
            creatorFor(specificationAttachRepository)
                    .instance(() -> new ProductSpecificationAttach(dto.getParentId(),specAttachInfo.getSpecId(),specAttachInfo.getCount(), FlagBit.of(specAttachInfo.getDeliveryFlag()).orElse(FlagBit.NO)))
                    .call();
        });

    }

    @Override
    public Page<ProductSpecificationVo> findByPage(PageRequestWrapper<ProductSpecificationReq> pageRequestWrapper) {
        List<PredicateAware> list = Lists.newArrayList();
        if(!StringUtils.isEmpty(pageRequestWrapper.getBean().getProductSpecName())){
            list.add(new ProductSpecPredicate.SpecNamePredicate(pageRequestWrapper.getBean().getProductSpecName()));
        }
        if(!StringUtils.isEmpty(pageRequestWrapper.getBean().getSpecCode())){
            list.add(new ProductSpecPredicate.ProductSpecCodePredicate(pageRequestWrapper.getBean().getSpecCode()));
        }
        Page<ProductSpecification> page = specificationRepository.findAll(PredicateUtils.buildListAndPredicate(list), PageRequest.of(pageRequestWrapper.getPage()-1,pageRequestWrapper.getSize()));
        return new PageImpl<>(page.getContent().stream().map(spc -> {
            Product product = productRepository.findById(spc.getProductId()).get();
            List<ProductSpecificationAttach> ats = specificationAttachRepository.findBySpecificationId(spc.getId());
            Integer hasChildren = 0;
            if(!CollectionUtils.isEmpty(ats)){
                hasChildren = 1;
            }
            ProductSpecificationVo vo = new ProductSpecificationVo(spc,productTypeRepository.findById(product.getProductTypeId()).get().getTypeName(),categoryRepository.findById(product.getCategoryId()).get().getName(),product.getProductName());
            vo.setHasChildren(hasChildren);
            return vo;
            })
            .collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
    }

    @Override
    public List<ProductSpecificationVo> findAll(ProductSpecificationReq req) {
        List<PredicateAware> list = Lists.newArrayList();
        if(!StringUtils.isEmpty(req.getProductSpecName())){
            list.add(new ProductSpecPredicate.SpecNamePredicate(req.getProductSpecName()));
        }
        if(!StringUtils.isEmpty(req.getSpecCode())){
            list.add(new ProductSpecPredicate.ProductSpecCodePredicate(req.getSpecCode()));
        }
        List<ProductSpecificationVo> vos = Lists.newArrayList();
        Iterable<ProductSpecification> specifications = specificationRepository.findAll(PredicateUtils.buildListAndPredicate(list));
        specifications.forEach(specification -> {
            Product product = productRepository.findById(specification.getProductId()).get();
            vos.add(new ProductSpecificationVo(specification,productTypeRepository.findById(product.getProductTypeId()).get().getTypeName(),categoryRepository.findById(product.getCategoryId()).get().getName(),product.getProductName()));
            });
        return vos;
    }

    @Override
    public List<ProductSpecificationVo> findAll() {
        Iterable<ProductSpecification> specifications = specificationRepository.findAll(QProductSpecification.productSpecification.validStatus.eq(ValidStatus.VALID));
        List<ProductSpecificationVo> vos = Lists.newArrayList();
        specifications.forEach(specification -> vos.add(new ProductSpecificationVo(specification)));
        return vos;
    }

    @Override
    public void validProductSpecification(Long id) {
        updaterFor(specificationRepository)
                .id(id)
                .update(specification -> specification.valid())
                .call();
    }

    @Override
    public void invalidProductSpecification(Long id) {
        updaterFor(specificationRepository)
                .id(id)
                .update(specification -> specification.invalid())
                .call();
    }

    @Override
    public void onlineProductSpecification(Long id) {
        updaterFor(specificationRepository)
                .id(id)
                .update(specification -> specification.onLine())
                .call();
    }

    @Override
    public void offLineProductSpecification(Long id) {
        updaterFor(specificationRepository)
                .id(id)
                .update(specification -> specification.offLine())
                .call();
    }


    @Override
    public List<SpecDictVo> findByStoreRestrict(Long storeId){
        List<StoreSpecRestrict> storeSpecRestricts = restrictRepository.findByStoreId(storeId);
        List<ProductSpecification> list = specificationRepository.findAllById(storeSpecRestricts.stream().map(ss -> ss.getSpecId()).collect(Collectors.toList()));
        return list.stream().map(ps -> new SpecDictVo(ps.getProductSpecName(),ps.getId(),ps.getPrice())).collect(Collectors.toList());
    }

    @Override
    public List<SpecDictVo> findAllOnlineSpec(ValidStatus online) {
        List<ProductSpecification> specs = specificationRepository.findByOnlineStatusAndValidStatus(online,ValidStatus.VALID);
        return specs.stream().map(ps -> new SpecDictVo(ps.getProductSpecName(),ps.getId(),ps.getPrice())).collect(Collectors.toList());
    }

    @Override
    public ProductSpecAttrModel findProductSpecModel(Long id) {
        Optional<ProductSpecification> specification = specificationRepository.findById(id);
        if(!specification.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        List<ProductAttribute> list = attributeRepository.findByProductId(specification.get().getProductId());
        List<SpecAttrModel> attributeVos = list.stream().map(attribute -> {
            List<String> values =  specAttrRepository.findByAttrId(attribute.getId()).stream().filter(sp -> sp.getProductSpecId().longValue() == id.longValue()).map(v -> v.getAttrValue()).collect(Collectors.toList());
            return new SpecAttrModel(attribute.getId(),attribute.getAttrName() , values);
        }).collect(Collectors.toList());

        List<ProductSpecificationAttach> attaches = specificationAttachRepository.findBySpecificationId(id);
        List<AttachModel> attachModels = Lists.newArrayList();
        attaches.stream().forEach(attach -> {
            Optional<ProductSpecification> ps =  specificationRepository.findById(attach.getSubSpecId());
            attachModels.add(new AttachModel(ps.get().getProductSpecName(),attach.getCount()));
        });
        Optional<Product> product = productRepository.findById(specification.get().getProductId());
        ProductSpecAttrModel model = new ProductSpecAttrModel();
        model.setAttributeList(attributeVos);
        model.setAttachModels(attachModels);
        model.setSpecificationVo(new ProductSpecificationVo(specification.get()));
        model.setProductVo(new ProductVo(product.get(),productTypeRepository.findById(product.get().getProductTypeId()).get().getTypeName(),categoryRepository.findById(product.get().getCategoryId()).get().getName()));
        return model;
    }


}
