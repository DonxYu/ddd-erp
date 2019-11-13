package cn.geekhalo.erp.domain.event;

import cn.geekhalo.erp.constants.InOutOperationType;
import cn.geekhalo.erp.domain.product.Goods;
import lombok.Value;

public class GoodsEvents {
    @Value
    public static class GoodsInEvent{
        private final Goods goods;
        private Long operateTime;
        private String providerName;
        private InOutOperationType inOutOperationType;
    }

    @Value
    public static class GoodsOutEvent{
        private final Goods goods;
        private Long operateTime;
        private InOutOperationType inOutOperationType;
    }


}
