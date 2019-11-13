package cn.geekhalo.erp.domain.storehouse.vo;

import cn.geekhalo.erp.domain.storehouse.StoreSpecificationSummary;
import lombok.Getter;

public class StoreSpecificationSummaryVo extends BaseStoreSpecificationSummaryVO {
    @Getter
    private String categoryName;
    @Getter
    private String typeName;
    @Getter
    private String specName;
    @Getter
    private String specCode;
    public StoreSpecificationSummaryVo(StoreSpecificationSummary summary){
        super(summary);
    }
    public StoreSpecificationSummaryVo(StoreSpecificationSummary summary,String categoryName,String typeName,String specName,String specCode){
        super(summary);
        this.categoryName = categoryName;
        this.typeName = typeName;
        this.specName = specName;
        this.specCode = specCode;
    }
}
