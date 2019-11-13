package cn.geekhalo.erp.repository.product;


import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.erp.domain.product.SpecificationAttr;

import java.util.List;

public interface SpecificationAttrRepository extends BaseRepository<SpecificationAttr,Long> {

    List<SpecificationAttr> findByProductSpecId(Long specId);

    List<SpecificationAttr> findByAttrId(Long attrId);

}
