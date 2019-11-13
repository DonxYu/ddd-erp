package cn.geekhalo.erp.domain.storehouse;

import cn.geekhalo.erp.domain.storehouse.vo.BaseStoreVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
public class StoreVo extends BaseStoreVO {

    @Getter
    @Setter
    private String storeTypeName;

    @Getter
    private List<StoreSpecRestrictWrapper> specRestrictWrapperList;

    @Getter
    private List<StoreUserWrapper> userWrappers;

    public StoreVo(Store store){
        super(store);
    }

    public StoreVo(Store store,List<StoreSpecRestrictWrapper> specRestrictWrapperList,List<StoreUserWrapper> userWrappers){
        super(store);
        this.specRestrictWrapperList = specRestrictWrapperList;
        this.userWrappers = userWrappers;
    }
}
