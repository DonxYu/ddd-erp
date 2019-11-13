package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.ProductAttribute;

import java.util.List;

public interface ProductAttributeRepository extends BaseRepository<ProductAttribute,Long> {

    List<ProductAttribute> findByProductId(Long productId);
}
