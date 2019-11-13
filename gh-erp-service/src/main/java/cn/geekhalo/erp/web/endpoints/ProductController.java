package cn.geekhalo.erp.web.endpoints;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.model.JsonObject;
import cn.geekhalo.common.validator.BaseValidator;
import cn.geekhalo.common.validator.Validate;
import cn.geekhalo.erp.domain.product.creator.CategoryCreator;
import cn.geekhalo.erp.domain.product.vo.ProductVo;
import cn.geekhalo.erp.dto.product.*;
import cn.geekhalo.erp.service.product.IProductCategoryService;
import cn.geekhalo.erp.service.product.IProductService;
import cn.geekhalo.erp.service.product.IProductTypeService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/product")
@Api(tags = "Product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductCategoryService categoryService;

    @Autowired
    private IProductTypeService typeService;

    @PostMapping(value = "add_product")
    @ApiOperation(value = "addProduct",notes = "添加产品信息")
    public ResponseEntity<JsonObject<String>> addProduct(@RequestBody CreateProductDto dto){
    	Validate validate =  BaseValidator.verifyDto(dto);
        if(!validate.pass()){
            throw new BusinessException(CodeEnum.ValidateError, JSON.toJSONString(validate.errors()));
        }
    	productService.create(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }



    @PostMapping(value = "find_by_id/{id}")
    @ApiOperation(value = "findById",notes = "根据id查询")
    public ResponseEntity<JsonObject<ProductVo>> findById(@PathVariable  Long id){
        Optional<ProductVo> product = productService.findById(id);
        return ResponseEntity.ok(JsonObject.success(product.get()));
    }

    @PostMapping(value = "find_by_page")
    @ApiOperation(value = "findByPage",notes = "根据条件分页查询")
    public ResponseEntity<JsonObject<Page<ProductVo>>> findByPage(@RequestBody PageRequestWrapper<ProductQueryReq> requestWrapper){
        Page<ProductVo> page = productService.queryProductByPage(requestWrapper);
        return ResponseEntity.ok(JsonObject.success(page));
    }

    @PostMapping(value = "add_product_type")
    @ApiOperation(value = "addProductType",notes = "添加产品类型")
    public ResponseEntity<JsonObject<String>> addProductType(@RequestBody ProductTypeAddDto dto){
    	Validate validate =  BaseValidator.verifyDto(dto);
        if(!validate.pass()){
            throw new BusinessException(CodeEnum.ValidateError, JSON.toJSONString(validate.errors()));
        }
    	typeService.addProductType(dto.getTypeName(),dto.getTypeCode());
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "add_product_category")
    @ApiOperation(value = "addProductCategory",notes = "添加产品类别")
    public ResponseEntity<JsonObject<String>> addProductCategory(@RequestBody ProductCategoryAddDto dto){
    	Validate validate =  BaseValidator.verifyDto(dto);
        if(!validate.pass()){
            throw new BusinessException(CodeEnum.ValidateError, JSON.toJSONString(validate.errors()));
        }
    	CategoryCreator creator = new CategoryCreator();
        creator.sortNum(dto.getSortNum());
        creator.name(dto.getCategoryName());
        creator.code(dto.getCategoryCode());
        categoryService.createCategory(creator);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "valid_product_category/{id}")
    @ApiOperation(value = "validProductCategory", notes = "启动类别")
    public ResponseEntity<JsonObject<String>> validProductCategory(@PathVariable Long id){
        categoryService.validCategory(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "invalid_product_category/{id}")
    @ApiOperation(value = "invalidProductCategory", notes = "禁用类别")
    public ResponseEntity<JsonObject<String>> invalidProductCategory(@PathVariable Long id){
        categoryService.invalidCategory(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "add_sub_category")
    @ApiOperation(value = "addSubCategory",notes = "增加子类别")
    public ResponseEntity<JsonObject<String>> addSubCategory(@RequestBody ProductCategoryAddDto dto){
    	Validate validate =  BaseValidator.verifyDto(dto);
        if(!validate.pass()){
            throw new BusinessException(CodeEnum.ValidateError, JSON.toJSONString(validate.errors()));
        }
    	CategoryCreator creator = new CategoryCreator();
        creator.sortNum(dto.getSortNum());
        creator.name(dto.getCategoryName());
        creator.code(dto.getCategoryCode());
        categoryService.addSubCategory(dto.getPid(),creator);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @GetMapping(value = "find_all_category")
    @ApiOperation(value = "findAllCategory",notes = "查找所有的产品类别")
    public ResponseEntity<JsonObject<List<ProductCategoryVo>>> findAllCategory(){
       List<ProductCategoryVo> list =  categoryService.findAllCategory().stream().map(pc -> new ProductCategoryVo(pc.getId(),pc.getName())).collect(Collectors.toList());
       return ResponseEntity.ok(JsonObject.success(list));
    }

    @GetMapping(value = "find_all_type")
    @ApiOperation(value = "findAllType" ,notes = "查找所有产品的类型")
    public ResponseEntity<JsonObject<List<ProductTypeVo>>> findAllType(){
        List<ProductTypeVo> list =  typeService.findAllType().stream().map(productType -> new ProductTypeVo(productType.getId(),productType.getTypeName())).collect(Collectors.toList());
        return ResponseEntity.ok(JsonObject.success(list));
    }

    @GetMapping(value = "find_product_model/{id}")
    @ApiOperation(value = "findProductModel" ,notes = "查找所有产品的类型")
    public ResponseEntity<JsonObject<ProductAttrModel>> findProductModel(@PathVariable Long id){
        return ResponseEntity.ok(JsonObject.success(productService.findProductModel(id)));
    }

    @PostMapping(value = "valid_product/{id}")
    @ApiOperation(value = "validProduct" ,notes = "启用商品")
    public ResponseEntity<JsonObject<String>> validProduct(@PathVariable Long id){
        productService.validProduct(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "invalid_product/{id}")
    @ApiOperation(value = "invalidProduct" ,notes = "禁用商品")
    public ResponseEntity<JsonObject<String>> invalidProduct(@PathVariable Long id){
        productService.invalidProduct(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }


    @Value
    static class ProductCategoryVo{
        private Long id;
        private String categoryName;
    }

    @Value
    static class ProductTypeVo{
        private Long id;
        private String typeName;
    }

}
