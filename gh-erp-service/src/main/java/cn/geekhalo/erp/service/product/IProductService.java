package cn.geekhalo.erp.service.product;

import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.erp.domain.product.vo.ProductVo;
import cn.geekhalo.erp.dto.product.AssembleProductDto;
import cn.geekhalo.erp.dto.product.CreateProductDto;
import cn.geekhalo.erp.dto.product.ProductAttrModel;
import cn.geekhalo.erp.dto.product.ProductQueryReq;
import org.springframework.data.domain.Page;
import java.util.Optional;


public interface IProductService {

    void create(CreateProductDto dto);

    Optional<ProductVo> findById(Long id);

    Page<ProductVo> queryProductByPage(PageRequestWrapper<ProductQueryReq> pageWrapper);

    ProductAttrModel findProductModel(Long id);

    void validProduct(Long id);

    void invalidProduct(Long id);

}
