package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.qsl.PredicateAware;
import cn.geekhalo.common.qsl.PredicateUtils;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.product.*;
import cn.geekhalo.erp.domain.product.vo.ProductAttributeValueVo;
import cn.geekhalo.erp.domain.product.vo.ProductAttributeVo;
import cn.geekhalo.erp.domain.product.vo.ProductVo;
import cn.geekhalo.erp.dto.product.*;
import cn.geekhalo.erp.repository.product.*;
import cn.geekhalo.erp.service.predicate.ProductPredicate;
import cn.geekhalo.erp.service.product.IProductService;
import com.google.common.collect.Lists;
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
public class ProductServiceImpl extends AbstractService implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductAttributeRepository attributeRepository;

    @Autowired
    private ProductAttributeValueRepository attributeValueRepository;

    protected ProductServiceImpl() {
        super(logger);
    }

    @Override
    @Transactional
    public void create(CreateProductDto dto) {
        Optional<ProductCategory> category = categoryRepository.findById(dto.getCategoryId());
        if(!category.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        Optional<ProductType> productType = productTypeRepository.findById(dto.getTypeId());
        if(!productType.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        Product createProduct = creatorFor(productRepository)
                .instance(() -> new Product())
                .update(product -> product.init())
                .update(product -> product.createProduct(dto))
                .call();
        logger.info("创建产品成功");
        logger.info("添加属性开始");
        List<AttrBean> list = dto.getAttrList();
        list.stream().forEach(
                ab -> {
                    ProductAttribute productAttribute = creatorFor(attributeRepository)
                            .instance(() -> new ProductAttribute())
                            .update(attr -> attr.createAttr(createProduct.getId(),ab))
                            .call();

                    ab.getAttrValues().forEach(av -> {
                        creatorFor(attributeValueRepository)
                                .instance(() -> new ProductAttributeValue(productAttribute.getId(),av))
                                .call();
                    });
                }
        );
        logger.info("添加属性结束");
    }




    @Override
    public Optional<ProductVo> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return Optional.of(new ProductVo(product.get(),productTypeRepository.findById(product.get().getProductTypeId()).get().getTypeName(),categoryRepository.findById(product.get().getCategoryId()).get().getName()));
        }
        return Optional.empty();
    }

    @Override
    public Page<ProductVo> queryProductByPage(PageRequestWrapper<ProductQueryReq> pageWrapper) {
        List<PredicateAware> list = Lists.newArrayList();
        if(!StringUtils.isEmpty(pageWrapper.getBean().getProductName())){
            list.add(new ProductPredicate.ProductNamePredicate(pageWrapper.getBean().getProductName()));
        }
        if(!Objects.isNull(pageWrapper.getBean().getCategoryId())){
            list.add(new ProductPredicate.ProductCategoryPredicate(pageWrapper.getBean().getCategoryId()));
        }
        if(!Objects.isNull(pageWrapper.getBean().getProductTypeId())){
            list.add(new ProductPredicate.ProductTypePredicate(pageWrapper.getBean().getProductTypeId()));
        }
//        list.add(new ProductPredicate.ProductStatusPredicate(ValidStatus.VALID.getCode()));
        Page<Product> page = productRepository.findAll(PredicateUtils.buildListAndPredicate(list), PageRequest.of(pageWrapper.getPage()-1,pageWrapper.getSize()));
        return new PageImpl<>(page.getContent().stream().map(product -> new ProductVo(product,productTypeRepository.findById(product.getProductTypeId()).get().getTypeName(),categoryRepository.findById(product.getCategoryId()).get().getName())).collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
    }

    @Override
    public ProductAttrModel findProductModel(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()){
            throw new BusinessException(CodeEnum.NotFindError);
        }
        List<ProductAttribute> list = attributeRepository.findByProductId(product.get().getId());
        List<ProductAttributeVo> attributeVos = list.stream().map(attribute -> {
           List<ProductAttributeValueVo> values =  attributeValueRepository.findByProductAttrId(attribute.getId()).stream().map(v -> new ProductAttributeValueVo(v)).collect(Collectors.toList());
           return new ProductAttributeVo(attribute , values);
        }).collect(Collectors.toList());
        ProductAttrModel model = new ProductAttrModel();
        model.setAttributeVoList(attributeVos);
        model.setProductVo(new ProductVo(product.get(),productTypeRepository.findById(product.get().getProductTypeId()).get().getTypeName(),categoryRepository.findById(product.get().getCategoryId()).get().getName()));
        return model;
    }

    @Override
    public void validProduct(Long id) {
        updaterFor(productRepository)
                .id(id)
                .update(product -> product.valid())
                .call();
    }

    @Override
    public void invalidProduct(Long id) {
        updaterFor(productRepository)
                .id(id)
                .update(product -> product.invalid())
                .call();
    }
}
