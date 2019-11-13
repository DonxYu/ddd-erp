package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.product.GoodsTraceLog;
import cn.geekhalo.erp.domain.product.creator.GoodsTraceLogCreator;
import cn.geekhalo.erp.repository.product.GoodsTraceLogRepository;
import cn.geekhalo.erp.service.product.IGoodsTraceLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsTraceLogServiceImpl  extends AbstractService implements IGoodsTraceLogService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsTraceLogServiceImpl.class);

    protected GoodsTraceLogServiceImpl() {
        super(logger);
    }

    @Autowired
    private GoodsTraceLogRepository goodsTraceLogRepository;


    @Override
    public void createTraceLog(GoodsTraceLogCreator creator) {
        creatorFor(goodsTraceLogRepository)
                .instance(() -> new GoodsTraceLog())
                .update(goodsTraceLog -> goodsTraceLog.doCreate(creator))
                .call();
    }
}
