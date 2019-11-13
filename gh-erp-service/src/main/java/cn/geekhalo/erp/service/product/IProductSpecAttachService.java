package cn.geekhalo.erp.service.product;


import cn.geekhalo.erp.domain.product.vo.ProductSpecAttachVo;

import java.util.List;

public interface IProductSpecAttachService {

    List<ProductSpecAttachVo> findBySpecId(Long id);
}
