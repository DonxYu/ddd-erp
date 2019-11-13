package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.product.ProductSpecificationAttach;
import cn.geekhalo.erp.domain.product.vo.ProductSpecAttachVo;
import cn.geekhalo.erp.repository.product.ProductSpecificationAttachRepository;
import cn.geekhalo.erp.repository.product.ProductSpecificationRepository;
import cn.geekhalo.erp.service.product.IProductSpecAttachService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductSpecAttachServiceImpl extends AbstractService implements IProductSpecAttachService {

    protected ProductSpecAttachServiceImpl() {
        super(log);
    }

    @Autowired
    private ProductSpecificationAttachRepository specificationAttachRepository;

    @Autowired
    private ProductSpecificationRepository specificationRepository;


    @Override
    public List<ProductSpecAttachVo> findBySpecId(Long id) {
        List<ProductSpecificationAttach> attaches = specificationAttachRepository.findBySpecificationId(id);
        List<ProductSpecAttachVo> list = Lists.newArrayList();
        attaches.forEach(attach -> {
            Optional<ProductSpecification> specification = specificationRepository.findById(attach.getSubSpecId());
            if(!specification.isPresent()){
                log.error("配件信息查询出错");
                throw new BusinessException(CodeEnum.NotFindError);
            }
            for(int i=0;i<attach.getCount();i++){
                ProductSpecAttachVo vo = new ProductSpecAttachVo(attach,specification.get().getProductSpecName());
                list.add(vo);
            }
        });
        return list;
    }
}
