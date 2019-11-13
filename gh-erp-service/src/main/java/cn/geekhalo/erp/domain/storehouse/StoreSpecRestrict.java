package cn.geekhalo.erp.domain.storehouse;

import cn.geekhalo.common.ddd.support.AbstractEntity;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "yd_store_spec_restrict")
@Entity
@QueryEntity
public class StoreSpecRestrict extends AbstractEntity {

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "spec_id")
    private Long specId;

    @Column(name = "specName")
    private String specName;

    public StoreSpecRestrict(Long storeId,Long specId,String specName){
        this.storeId = storeId;
        this.specId = specId;
        this.specName = specName;
    }
}
