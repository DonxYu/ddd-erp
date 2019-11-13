package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.domain.product.ProductType;
import cn.geekhalo.erp.domain.product.QProductType;
import cn.geekhalo.erp.repository.product.ProductTypeRepository;
import cn.geekhalo.erp.service.product.IProductTypeService;
import cn.geekhalo.erp.util.BizExceptionUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductTypeServiceImpl extends AbstractService implements IProductTypeService {

    private static final Logger logger = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    protected ProductTypeServiceImpl() {
        super(logger);
    }

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public void addProductType(String name,String typeCode) {
        BizExceptionUtils.checkEmpty(name, ErrorMsg.ProductTypeNotNull);
        creatorFor(productTypeRepository)
                .instance(() -> new ProductType(name,typeCode))
                .call();
    }

    @Override
    public void validProductType(Long id) {
        updaterFor(productTypeRepository)
                .id(id)
                .update(productType -> productType.valid())
                .call();
    }

    @Override
    public void invalidProductType(Long id) {
        updaterFor(productTypeRepository)
                .id(id)
                .update(productType -> productType.invalid())
                .call();
    }

    @Override
    public List<ProductType> findAllType() {
        Iterable<ProductType> iterable = productTypeRepository.findAll(QProductType.productType.validStatus.eq(ValidStatus.VALID));
        List<ProductType> list = Lists.newArrayList();
        iterable.forEach(
                it -> list.add(it)
        );
        return list;
    }
}
