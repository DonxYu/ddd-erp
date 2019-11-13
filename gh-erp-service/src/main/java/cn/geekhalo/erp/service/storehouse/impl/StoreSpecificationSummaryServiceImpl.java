package cn.geekhalo.erp.service.storehouse.impl;

import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.product.Product;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.storehouse.QStoreSpecificationSummary;
import cn.geekhalo.erp.domain.storehouse.StoreSpecificationSummary;
import cn.geekhalo.erp.domain.storehouse.vo.StoreSpecificationSummaryVo;
import cn.geekhalo.erp.dto.product.InitSpecificationDto;
import cn.geekhalo.erp.dto.storehouse.StoreSummaryQuery;
import cn.geekhalo.erp.repository.product.ProductCategoryRepository;
import cn.geekhalo.erp.repository.product.ProductRepository;
import cn.geekhalo.erp.repository.product.ProductSpecificationRepository;
import cn.geekhalo.erp.repository.product.ProductTypeRepository;
import cn.geekhalo.erp.repository.storehouse.StoreSpecificationSummaryRepository;
import cn.geekhalo.erp.service.storehouse.IStoreSpecificationSummaryService;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreSpecificationSummaryServiceImpl extends AbstractService implements IStoreSpecificationSummaryService {

    protected StoreSpecificationSummaryServiceImpl() {
        super(log);
    }

    @Autowired
    private StoreSpecificationSummaryRepository repository;

    @Autowired
    private ProductSpecificationRepository specificationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private ProductTypeRepository typeRepository;

    @Override
    public void createStoreSpecificationSummary(InitSpecificationDto dto) {
        creatorFor(repository)
                .instance(() -> new StoreSpecificationSummary())
                .update(summary -> summary.initSpecificationSummary(dto))
                .call();
    }

    @Override
    public void addStockAndPrice(Long summaryId,Integer stock, BigDecimal price) {
        updaterFor(repository)
                .id(summaryId)
                .update(summary -> summary.addtockAndPrice(stock,price))
                .call();
    }

    @Override
    public void subStockAndPrice(Long summaryId,Integer stock, BigDecimal price) {
        updaterFor(repository)
                .id(summaryId)
                .update(summary -> summary.subStractAndPrice(stock,price))
                .call();
    }

    @Override
    public Optional<StoreSpecificationSummary> findByStoreAndSpecificationId(Long storeId, Long specId) {
        return repository.findByStoreIdAndSpecificationId(storeId,specId);
    }

    @Override
    public Page<StoreSpecificationSummaryVo> findByPage(PageRequestWrapper<StoreSummaryQuery> pageRequestWrapper) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.nonNull(pageRequestWrapper.getBean().getSpecId())){
            booleanBuilder.and(QStoreSpecificationSummary.storeSpecificationSummary.storeId.eq(pageRequestWrapper.getBean().getStoreId()));
        }
        if(Objects.nonNull(pageRequestWrapper.getBean().getSpecId())){
            booleanBuilder.and(QStoreSpecificationSummary.storeSpecificationSummary.specificationId.eq(pageRequestWrapper.getBean().getSpecId()));
        }
        Page<StoreSpecificationSummary> page = repository.findAll(booleanBuilder, PageRequest.of(pageRequestWrapper.getPage()-1,pageRequestWrapper.getSize()));
        return new PageImpl<>(
                page.getContent().stream()
                .map(summary -> {
                    Optional<ProductSpecification> specification = specificationRepository.findById(summary.getSpecificationId());
                    Optional<Product> product = productRepository.findById(specification.get().getProductId());
                    return new StoreSpecificationSummaryVo(
                            summary,
                            categoryRepository.findById(product.get().getCategoryId()).get().getName(),
                            typeRepository.findById(product.get().getProductTypeId()).get().getTypeName(),
                            specification.get().getProductSpecName(),
                            specification.get().getSpecCode()
                    );
                }).collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );
    }
}
