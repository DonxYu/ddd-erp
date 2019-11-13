package cn.geekhalo.erp.service.product;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.product.vo.ProductSpecificationVo;
import cn.geekhalo.erp.domain.product.vo.SpecDictVo;
import cn.geekhalo.erp.dto.product.AssembleSpecDto;
import cn.geekhalo.erp.dto.product.ProductSpecAttrModel;
import cn.geekhalo.erp.dto.product.ProductSpecificationInitDto;
import cn.geekhalo.erp.dto.product.ProductSpecificationReq;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IProductSpecificationService {

    Optional<ProductSpecification> findById(Long id);

    Optional<ProductSpecification> findBySpecCode(String specCode);

    ProductSpecification initSpecification(ProductSpecificationInitDto dto);

    void assembleSpecification(AssembleSpecDto dto);

    Page<ProductSpecificationVo> findByPage(PageRequestWrapper<ProductSpecificationReq> pageRequestWrapper);

    List<ProductSpecificationVo> findAll(ProductSpecificationReq req);

    List<ProductSpecificationVo> findAll();

    void validProductSpecification(Long id);

    void invalidProductSpecification(Long id);

    void onlineProductSpecification(Long id);

    void offLineProductSpecification(Long id);

    List<SpecDictVo> findAllOnlineSpec(ValidStatus online);

    List<SpecDictVo> findByStoreRestrict(Long storeId);

    ProductSpecAttrModel findProductSpecModel(Long id);
}
