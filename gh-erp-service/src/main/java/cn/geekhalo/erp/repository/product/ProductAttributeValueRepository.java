package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.ProductAttributeValue;

import java.util.List;

public interface ProductAttributeValueRepository extends BaseRepository<ProductAttributeValue,Long> {

    List<ProductAttributeValue> findByProductAttrId(Long productAttrId);
}
