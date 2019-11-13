package cn.geekhalo.erp.domain.storehouse;

import cn.geekhalo.common.ddd.support.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "yd_store_record_goods")
@Data
public class StoreRecordGoods extends AbstractEntity {

    @Column(name = "store_in_out_id")
    private Long storeInOutId;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "bar_code")
    private String barCode;

    public StoreRecordGoods(Long storeInOutId,Long goodsId,String goodsName,String barCode){
        setStoreInOutId(storeInOutId);
        setGoodsId(goodsId);
        setGoodsName(goodsName);
        setBarCode(barCode);
    }

}
