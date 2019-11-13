package cn.geekhalo.erp.domain.product;

import cn.geekhalo.codegen.creator.GenCreator;
import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.erp.constants.InOutOperationType;
import cn.geekhalo.erp.constants.TraceType;
import cn.geekhalo.erp.domain.product.creator.GoodsTraceLogCreator;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "tb_goods_trace_log")
@GenCreator
@GenVO
public class GoodsTraceLog extends AbstractEntity {

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "trace_type")
    private TraceType traceType;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "agent_Id")
    private Long agentId;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "log_desc")
    private String logDesc;

    @Column(name = "flow_water")
    private Long flowWater;

    @Column(name = "operation_type")
    private InOutOperationType operationType;

    public void  doCreate(GoodsTraceLogCreator creator){
        creator.accept(this);
    }
}
