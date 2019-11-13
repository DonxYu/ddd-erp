package cn.geekhalo.erp.service.product;


import cn.geekhalo.erp.domain.product.creator.GoodsTraceLogCreator;

public interface IGoodsTraceLogService {
    void createTraceLog(GoodsTraceLogCreator creator);
}
