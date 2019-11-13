package cn.geekhalo.erp.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class AttrWrapper {
    private Long attrId;
    private String attrName;
    private List<AttrValues> attrList;
    private String attrCode;
}
