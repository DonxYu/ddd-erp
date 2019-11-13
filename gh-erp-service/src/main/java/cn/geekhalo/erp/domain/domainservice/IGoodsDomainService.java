package cn.geekhalo.erp.domain.domainservice;


import cn.geekhalo.common.model.JsonObject;

import java.util.List;

public interface IGoodsDomainService {
    JsonObject<List<String>> check(List<String> barCodeList);

    void checkGoodsValid(Long storeId, List<String> barCodes);
}
