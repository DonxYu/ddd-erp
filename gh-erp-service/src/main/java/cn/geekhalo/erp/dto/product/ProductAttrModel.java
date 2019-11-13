package cn.geekhalo.erp.dto.product;

import cn.geekhalo.erp.domain.product.vo.ProductAttributeVo;
import cn.geekhalo.erp.domain.product.vo.ProductVo;
import lombok.Data;

import java.util.List;

@Data
public class ProductAttrModel {

    private List<ProductAttributeVo> attributeVoList;

    private ProductVo productVo;
}
