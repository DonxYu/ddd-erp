package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.ProductSpecificationAttach;

import java.util.List;

public interface ProductSpecificationAttachRepository extends BaseRepository<ProductSpecificationAttach,Long> {
    List<ProductSpecificationAttach> findBySpecificationId(Long id);
}
