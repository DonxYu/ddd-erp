package cn.geekhalo.erp.dto.product;

import cn.geekhalo.erp.domain.product.vo.ProductSpecificationVo;
import cn.geekhalo.erp.domain.product.vo.ProductVo;
import lombok.Data;

import java.util.List;

@Data
public class ProductSpecAttrModel {

    private List<SpecAttrModel> attributeList;

    private ProductVo productVo;

    private List<AttachModel> attachModels;

    private ProductSpecificationVo specificationVo;
}
