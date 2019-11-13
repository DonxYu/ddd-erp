package cn.geekhalo.erp.service.product.impl;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.product.ProductCategory;
import cn.geekhalo.erp.domain.product.QProductCategory;
import cn.geekhalo.erp.domain.product.creator.CategoryCreator;
import cn.geekhalo.erp.repository.product.ProductCategoryRepository;
import cn.geekhalo.erp.service.product.IProductCategoryService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductCategoryServiceImpl extends AbstractService implements IProductCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);


    protected ProductCategoryServiceImpl() {
        super(logger);
    }

    @Autowired
    private ProductCategoryRepository productCategoryRepository;



    @Override
    public void createCategory(CategoryCreator creator) {
        creatorFor(productCategoryRepository)
                .instance(() -> new ProductCategory())
                .update(category -> category.init())
                .update(category -> category.doCreate(creator))
                .call();
    }

    @Override
    public void addSubCategory(Long pid, CategoryCreator creator) {
        creatorFor(productCategoryRepository)
                .instance(() -> new ProductCategory())
                .update(pc -> pc.doSubCategoryCreate(creator,pid))
                .call();

    }

    @Override
    public void validCategory(Long id) {
        updaterFor(productCategoryRepository)
                .id(id)
                .update(category -> category.valid())
                .call();
    }

    @Override
    public void invalidCategory(Long id) {
        updaterFor(productCategoryRepository)
                .id(id)
                .update(category -> category.invalid())
                .call();
    }

    @Override
    public List<ProductCategory> findAllCategory() {
        Iterable<ProductCategory> iterable = productCategoryRepository.findAll(QProductCategory.productCategory.validStatus.eq(ValidStatus.VALID));
        List<ProductCategory> list = Lists.newArrayList();
        iterable.forEach(
                it -> list.add(it)
        );
        return list;
    }

}
