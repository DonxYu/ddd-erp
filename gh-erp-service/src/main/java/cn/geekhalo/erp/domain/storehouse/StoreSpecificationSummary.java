package cn.geekhalo.erp.domain.storehouse;

import cn.geekhalo.codegen.vo.GenVO;
import cn.geekhalo.common.ddd.support.AbstractEntity;
import cn.geekhalo.erp.dto.product.InitSpecificationDto;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_store_specification_summary")
@ToString(callSuper = true)
@GenVO
@Data
public class StoreSpecificationSummary extends AbstractEntity {

    @Column(name = "specification_name")
    private String specificationName;
    @Column(name = "store_id")
    private Long storeId;
    @Column(name = "specification_id")
    private Long specificationId;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "history_total_count")
    private Integer historyTotalCount;

    public void initSpecificationSummary(InitSpecificationDto dto){
        setSpecificationName(dto.getSpecificationName());
        setStoreId(dto.getStoreId());
        setTotalPrice(dto.getTotalPrice());
        setStock(dto.getStock());
        setSpecificationId(dto.getSpecificationId());
        setHistoryTotalCount(dto.getStock());
    }

    public void addtockAndPrice(Integer stock,BigDecimal price){
        setStock(getStock() + stock);
        setHistoryTotalCount(getHistoryTotalCount() + stock);
        setTotalPrice(NumberUtil.add(getTotalPrice(),price));
    }

    public void subStractAndPrice(Integer stock,BigDecimal price){
        setStock(getStock() - stock);
        setTotalPrice(NumberUtil.sub(getTotalPrice(),price));
    }
}
