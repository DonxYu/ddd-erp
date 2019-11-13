package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.creator.GenCreator;
import cn.geekhalo.codegen.soaupdater.GenUpdater;
import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.constants.GoodsType;
import cn.geekhalo.common.constants.GoodsTypeConverter;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.constants.ValidStatusConverter;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.erp.constants.ErrorMsg;
import cn.geekhalo.erp.constants.InOutOperationType;
import cn.geekhalo.erp.domain.event.GoodsEvents;
import cn.geekhalo.erp.util.BizExceptionUtils;
import lombok.*;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Data
@Table(name = "tb_goods")
@GenCreator
@GenUpdater
@GenVO
@ToString(callSuper = true)
public class Goods extends AbstractEntity {

    @Description(value = "条码")
    @Column(name = "bar_code")
    private String barCode;

    @Convert(converter = GoodsTypeConverter.class)
    @Column(name = "goods_type")
    private GoodsType goodsType;

    @Column(name = "valid_status")
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    @Column(name="parent_id")
    @Setter(AccessLevel.PROTECTED)
    private Long parentId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "product_specification_id")
    private Long specificationId;

    @Column(name = "batch_id")
    @Description(value = "批次号-》代表一个批次")
    private Long batchId;

    @Column(name = "operate_user")
    private String operateUser;

    @Column(name = "provider_name")
    private String providerName;

    private void goodsIn(GoodsCreateDto object,Long storeId, Long specificationId,Long batchId){
        BizExceptionUtils.checkEmpty(object.getBarCode(), ErrorMsg.BarCodeNotNull);
        setValidStatus(ValidStatus.VALID);
        setBarCode(object.getBarCode());
        setOperateUser(object.getOperateUser());
        setStoreId(storeId);
        setProviderName(object.getProviderName());
        setBatchId(batchId);
        setSpecificationId(specificationId);
    }

    private void goodsOut(Long batchId,String operateUser){
        if(Objects.equals(ValidStatus.INVALID,getValidStatus())){
            throw new BusinessException(ErrorMsg.GoodsNotAvailable);
        }
        setBatchId(batchId);
        setOperateUser(operateUser);
        setValidStatus(ValidStatus.INVALID);
    }

    //商品零部件入库
    public void goodsBasicInStore(GoodsCreateDto object,Long storeId, Long specificationId,Long batchId){
        goodsIn(object,storeId,specificationId,batchId);
        setGoodsType(GoodsType.GOODS_BASIC);
        registerEvent(new GoodsEvents.GoodsInEvent(this,object.getOperateTime(),object.getProviderName(), InOutOperationType.IN_PURCHASE));
    }

    public void goodsTransferInStore(GoodsCreateDto object,Long storeId, Long specificationId,Long batchId,GoodsType type){
        goodsIn(object,storeId,specificationId,batchId);
        setGoodsType(type);
        registerEvent(new GoodsEvents.GoodsInEvent(this,object.getOperateTime(),object.getProviderName(),InOutOperationType.IN_TRANSFER));
    }

    public void goodsBuyInStore(GoodsCreateDto object,Long storeId, Long specificationId,Long batchId,GoodsType type){
        goodsIn(object,storeId,specificationId,batchId);
        setGoodsType(type);
        registerEvent(new GoodsEvents.GoodsInEvent(this,object.getOperateTime(),object.getProviderName(),InOutOperationType.IN_PURCHASE));
    }


    public void goodsTransferOutStore(Long batchId,String operateUser){
        goodsOut(batchId,operateUser);
        registerEvent(new GoodsEvents.GoodsOutEvent(this,Instant.now().toEpochMilli(),InOutOperationType.OUT_TRANSFER));
    }

    //商品生产入库
    public void goodsMakeInStore(Long storeId,Long specificationId,GoodsCreateDto dto,Long batchId){
        goodsIn(dto,storeId,specificationId,batchId);
        setGoodsType(GoodsType.GOODS_MAKE);
        registerEvent(new GoodsEvents.GoodsInEvent(this,dto.getOperateTime(),dto.getProviderName(),InOutOperationType.IN_MAKE));
    }

    //商品配货入库
    public void goodsAssembleInStore(Long storeId,Long specificationId,GoodsCreateDto dto,Long batchId){
        goodsIn(dto,storeId,specificationId,batchId);
        setGoodsType(GoodsType.GOODS_ASSEMBLE);
        registerEvent(new GoodsEvents.GoodsInEvent(this,dto.getOperateTime(),dto.getProviderName(),InOutOperationType.IN_ASSEMBLE));
    }

    public void goodsBadInStore(Long storeId,Long specificationId,GoodsCreateDto dto,Long batchId){
        goodsIn(dto,storeId,specificationId,batchId);
        setGoodsType(GoodsType.GOODS_BAD);
        registerEvent(new GoodsEvents.GoodsInEvent(this,dto.getOperateTime(),dto.getProviderName(),InOutOperationType.IN_BAD));
    }

    public void goodsAssembleUsed(Long batchId,String operateUser){
        goodsOut(batchId,operateUser);
        registerEvent(new GoodsEvents.GoodsOutEvent(this,Instant.now().toEpochMilli(),InOutOperationType.OUT_ASSEMBLE));
    }

    public void goodsMakeUsed(Long batchId,String operateUser){
        goodsOut(batchId,operateUser);
        registerEvent(new GoodsEvents.GoodsOutEvent(this, Instant.now().toEpochMilli(),InOutOperationType.OUT_MAKE_USED));
    }

    public void goodsSaleToAgent(Long batchId,String operateUser){
        goodsOut(batchId,operateUser);
        registerEvent(new GoodsEvents.GoodsOutEvent(this, Instant.now().toEpochMilli(),InOutOperationType.OUT_SALE_TO_AGENT));
    }
    public void goodSaleToUser(Long batchId,String operateUser){
        goodsOut(batchId,operateUser);
        registerEvent(new GoodsEvents.GoodsOutEvent(this, Instant.now().toEpochMilli(),InOutOperationType.OUT_SALE_TO_USER));
    }

    public void goodsTransferToOtherStore(Long batchId,String operateUser){
        goodsOut(batchId,operateUser);
        registerEvent(new GoodsEvents.GoodsOutEvent(this, Instant.now().toEpochMilli(),InOutOperationType.OUT_TRANSFER));
    }

    public void parentId(Long pid){
        setParentId(pid);
    }
//    //将生产件置为配货使用
//    public void  goodsStatusToAssembleUsed(){
//        setValidStatus(ValidStatus.INVALID);
//        setGoodsStatus(GoodsStatus.ASSEMBLE_USED);
//    }

    @Value
    public static class GoodsCreateDto{
        private String barCode;
        private String operateUser;
        private Long operateTime;
        private String providerName;
    }

}
