package cn.geekhalo.erp.service.product;


import cn.geekhalo.erp.domain.product.ProductType;

import java.util.List;

public interface IProductTypeService {
    void addProductType(String name, String typeCode);

    void validProductType(Long id);

    void invalidProductType(Long id);

    List<ProductType> findAllType();
}
