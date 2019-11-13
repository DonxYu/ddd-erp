package cn.geekhalo.erp.domain.domainservice.impl;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.model.JsonObject;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.domain.domainservice.IGoodsDomainService;
import cn.geekhalo.erp.domain.product.Goods;
import cn.geekhalo.erp.repository.product.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsDomainServiceImpl extends AbstractService implements IGoodsDomainService {
    private static  final Logger logger = LoggerFactory.getLogger(GoodsDomainServiceImpl.class);

    protected GoodsDomainServiceImpl() {
        super(logger);
    }

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public JsonObject<List<String>> check(List<String> barCodeList) {
        List<Goods> goods = goodsRepository.findGoodsByBarCodeIn(barCodeList);
        if(CollectionUtils.isEmpty(goods)){
            return JsonObject.success(Lists.emptyList(),"没有重复的条码");
        }else {
            return JsonObject.fail(goods.stream().map(g -> g.getBarCode()).collect(Collectors.toList()),"有重复的条码");
        }
    }

    @Override
    public void checkGoodsValid(Long storeId, List<String> barCodes) {
        log.info("校验商品是否在库房中");
        barCodes.forEach(bar -> {
            Optional<Goods> goods = goodsRepository.findGoodsByBarCodeAndStoreIdAndValidStatus(bar,storeId, ValidStatus.VALID);
            if(!goods.isPresent()){
                throw new BusinessException(ErrorMsg.GoodsNotExist,bar);
            }
        });
    }
}
