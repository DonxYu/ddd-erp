package cn.geekhalo.erp.domain.product.vo;

import cn.geekhalo.erp.domain.product.SpecificationAttr;
import lombok.Getter;

import java.util.List;

public class SpecificationAttrVo extends BaseSpecificationAttrVO {

    public SpecificationAttrVo(SpecificationAttr attr){
        super(attr);
    }

    @Getter
    private List<String> values;

    public SpecificationAttrVo(SpecificationAttr attr,List<String> values){
        super(attr);
        this.values = values;
    }
}
