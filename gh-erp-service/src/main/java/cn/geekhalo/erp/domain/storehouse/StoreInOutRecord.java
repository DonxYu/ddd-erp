package cn.geekhalo.erp.domain.storehouse;

import cn.geekhalo.codegen.vo.Description;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.erp.constants.*;
import cn.geekhalo.erp.dto.storehouse.CreateStoreRecordDto;
import cn.geekhalo.erp.util.WaterFlowUtils;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "yd_store_in_out_record")
@ToString(callSuper = true)
@GenVO
public class StoreInOutRecord extends AbstractEntity {

    @Description(value = "操作类型")
    @Convert(converter = InOutOperationTypeConverter.class)
    @Column(name = "operation_type")
    private InOutOperationType operationType;

    @Description(value = "")
    @Column(name = "direction_type")
    @Convert(converter = InOutDirectionTypeConverter.class)
    private InOutDirectionType directionType;

    @Description(value = "总价")
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Description(value = "数量")
    @Column(name = "count")
    private Integer count;

    @Description(value = "备注")
    @Column(name = "remark")
    private String remark;

    @Column(name = "spec_id")
    private Long specificationId;

    @Column(name = "water_flow_number")
    private Long waterFlowNumber;

    @Column(name = "goods_batch_id")
    private Long goodsBatchId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "operate_user")
    private String operateUser;

    @Column(name = "operate_time")
    private Long operateTime;

    @Column(name = "provider_name")
    private String providerName;

    //添加入库记录--> 注册入库事件
    public void addRecord(CreateStoreRecordDto dto, Long specificationId, Long storeId){
        setDirectionType(dto.getDirectionType());
        setOperationType(dto.getOperationType());
        setRemark(dto.getRemark());
        setStoreId(storeId);
        setSpecificationId(specificationId);
        setWaterFlowNumber(WaterFlowUtils.nextWaterFlow());
        setCount(1);
        setGoodsBatchId(dto.getBatchId());
        setTotalPrice(dto.getPrice());
        setProviderName(dto.getProviderName());
        setOperateTime(dto.getOperateTime());
        if(StringUtils.isNotEmpty(dto.getOperateUser())){
            setOperateUser(dto.getOperateUser());
        }else {
            setOperateUser(ErpConstants.SYSTEM);
        }
    }

    //更新价格和数量(根据批次）
    public void updatePriceAndCount(BigDecimal price, Integer count){
        setTotalPrice(NumberUtil.add(getTotalPrice(),price));
        setCount(getCount() + count);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "-" + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StoreInOutRecord that = (StoreInOutRecord) o;
        return Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(count, that.count) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(waterFlowNumber, that.waterFlowNumber) &&
                Objects.equals(goodsBatchId, that.goodsBatchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalPrice, count, remark, waterFlowNumber, goodsBatchId);
    }
}
