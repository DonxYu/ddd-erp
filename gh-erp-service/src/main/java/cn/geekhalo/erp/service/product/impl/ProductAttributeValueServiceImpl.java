package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.product.ProductAttributeValue;
import cn.geekhalo.erp.repository.product.ProductAttributeValueRepository;
import cn.geekhalo.erp.service.product.IProductAttributeValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductAttributeValueServiceImpl extends AbstractService implements IProductAttributeValueService {

    protected ProductAttributeValueServiceImpl() {
        super(log);
    }

    @Autowired
    private ProductAttributeValueRepository attributeValueRepository;


    @Override
    public void createAttributeValue(Long attrId, String value) {
        creatorFor(attributeValueRepository)
                .instance(() -> new ProductAttributeValue(attrId,value))
                .call();
    }
}
