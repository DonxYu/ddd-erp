package cn.geekhalo.erp.service.product;

import cn.geekhalo.erp.domain.product.ProductCategory;
import cn.geekhalo.erp.domain.product.creator.CategoryCreator;

import java.util.List;

public interface IProductCategoryService {
    void createCategory(CategoryCreator creator);

    void addSubCategory(Long pid, CategoryCreator creator);

    void validCategory(Long id);

    void invalidCategory(Long id);

    List<ProductCategory> findAllCategory();
}
