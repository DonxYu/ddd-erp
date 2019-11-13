package cn.geekhalo.erp.constants;

import cn.geekhalo.common.constants.BaseEnum;
import lombok.Getter;
import java.util.Optional;

/**
 * 出入库操作类型
 */
public enum InOutOperationType implements BaseEnum<InOutOperationType> {
    IN_PURCHASE(1,"采购入库"),
    IN_ASSEMBLE(2,"配货入库"),
    IN_MAKE(3,"生产入库"),
    IN_TRANSFER(4,"转仓入库"),
    IN_INVENTORY(5,"盘点调仓入库"),
    IN_SALE_RETURN(6,"销售退货"),
    OUT_PURCHASE_RETURN(7,"采购退货(出库)"),
    OUT_SALE_TO_AGENT(8,"销售给代理商"),
    OUT_SALE_TO_USER(9,"销售给用户"),
    OUT_TRANSFER(10,"转仓出库"),
    OUT_INVENTORY(11,"盘点调仓出库"),
    OUT_ASSEMBLE(12,"组装出库"),
    OUT_MAKE_USED(13,"生产出库"),
    IN_BAD(14,"坏件入库"),
    ;
    @Getter
    private Integer code;
    @Getter
    private String msg;
    InOutOperationType(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return msg;
    }

    public static Optional<InOutOperationType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(InOutOperationType.class,code));
    }
}
