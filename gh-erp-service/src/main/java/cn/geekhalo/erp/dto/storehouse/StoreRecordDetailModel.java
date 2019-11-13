package cn.geekhalo.erp.dto.storehouse;

import cn.geekhalo.erp.domain.storehouse.vo.StoreRecordVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StoreRecordDetailModel {
    private StoreRecordVo recordVo;
    private List<String> barCodes;
}
