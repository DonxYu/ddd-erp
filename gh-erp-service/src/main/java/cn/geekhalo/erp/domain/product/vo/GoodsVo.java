package cn.geekhalo.erp.domain.product.vo;

import cn.geekhalo.erp.domain.product.Goods;
import lombok.Getter;

public class GoodsVo extends BaseGoodsVO {
    @Getter
    private String goodsName;

    @Getter
    private String storeName;


    public GoodsVo(Goods goods){
        super(goods);
    }

    public GoodsVo(Goods goods ,String goodsName){
        super(goods);
        this.goodsName = goodsName;
    }

    public GoodsVo(Goods goods,String goodsName,String storeName){
        super(goods);
        this.goodsName = goodsName;
        this.storeName = storeName;
    }
}
