package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.repository.product.ProductAttributeRepository;
import cn.geekhalo.erp.service.product.IProductAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeServiceImpl extends AbstractService implements IProductAttributeService {

    private static final Logger logger = LoggerFactory.getLogger(ProductAttributeServiceImpl.class);

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    protected ProductAttributeServiceImpl() {
        super(logger);
    }


    @Override
    public void createProductAttribute() {

    }
}
