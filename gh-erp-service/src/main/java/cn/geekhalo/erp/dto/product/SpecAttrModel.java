package cn.geekhalo.erp.dto.product;

import lombok.Value;
import java.util.List;

@Value
public class SpecAttrModel {
    private Long attrId;
    private String attrName;
    private List<String> attrValues;
}
