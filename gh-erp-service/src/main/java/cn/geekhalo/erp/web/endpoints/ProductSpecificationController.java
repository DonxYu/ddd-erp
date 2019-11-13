package cn.geekhalo.erp.web.endpoints;


import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.model.JsonObject;
import cn.geekhalo.erp.domain.product.ProductSpecification;
import cn.geekhalo.erp.domain.product.vo.ProductSpecificationVo;
import cn.geekhalo.erp.domain.product.vo.SpecDictVo;
import cn.geekhalo.erp.domain.storehouse.vo.StoreSpecificationSummaryVo;
import cn.geekhalo.erp.dto.product.AssembleSpecDto;
import cn.geekhalo.erp.dto.product.ProductSpecAttrModel;
import cn.geekhalo.erp.dto.product.ProductSpecificationInitDto;
import cn.geekhalo.erp.dto.product.ProductSpecificationReq;
import cn.geekhalo.erp.dto.storehouse.StoreSummaryQuery;
import cn.geekhalo.erp.service.product.IProductSpecAttachService;
import cn.geekhalo.erp.service.product.IProductSpecificationService;
import cn.geekhalo.erp.service.storehouse.IStoreSpecificationSummaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "v1/specification")
@Api(tags = "ProductSpecification")
public class ProductSpecificationController {

    @Autowired
    private IProductSpecificationService specificationService;

    @Autowired
    private IProductSpecAttachService attachService;

    @Autowired
    private IStoreSpecificationSummaryService storeSpecificationSummaryService;


    @PostMapping(value = "init_specification")
    @ApiOperation(value = "initSpecification",notes = "初始化规格")
    public ResponseEntity<JsonObject<String>> initSpecification(@RequestBody ProductSpecificationInitDto dto){
        specificationService.initSpecification(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "assemble_specification")
    @ApiOperation(value = "assembleSpecification",notes = "组装规格")
    public ResponseEntity<JsonObject<String>> assembleSpecification(@RequestBody AssembleSpecDto dto){
        specificationService.assembleSpecification(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }


    @GetMapping(value = "find_by_id/{id}")
    @ApiOperation(value = "findById",notes = "根据id查询规格信息")
    public ResponseEntity<JsonObject<ProductSpecificationVo>> findById(@PathVariable Long id){
       Optional<ProductSpecification> specification =  specificationService.findById(id);
       if(specification.isPresent()){
           return ResponseEntity.ok(JsonObject.success(new ProductSpecificationVo(specification.get())));
       }else {
           return ResponseEntity.ok(JsonObject.success(null));
       }
    }

    @GetMapping(value = "find_spec_model/{id}")
    @ApiOperation(value = "findProductSpecModel",notes = "根据id查询规格明细")
    public ResponseEntity<JsonObject<ProductSpecAttrModel>> findProductSpecModel(@PathVariable Long id){
        ProductSpecAttrModel model =  specificationService.findProductSpecModel(id);
        return ResponseEntity.ok(JsonObject.success(model));
    }

    @PostMapping(value = "find_by_page")
    @ApiOperation(value = "findByPage",notes = "根据条件分页查询")
    public ResponseEntity<JsonObject<Page<ProductSpecificationVo>>> findByPage(@RequestBody PageRequestWrapper<ProductSpecificationReq> pageRequestWrapper){
        Page<ProductSpecificationVo> page = specificationService.findByPage(pageRequestWrapper);
        return ResponseEntity.ok(JsonObject.success(page));
    }


    @PostMapping(value = "find_all")
    @ApiOperation(value = "findAll",notes = "查询所有的规格")
    public ResponseEntity<JsonObject<List<ProductSpecificationVo>>> findAll(){
        List<ProductSpecificationVo> vos = specificationService.findAll();
        return ResponseEntity.ok(JsonObject.success(vos));
    }



    @GetMapping(value = "find_by_spec_code{code}")
    @ApiOperation(value = "findBySpecCode",notes = "根据code查找")
    public ResponseEntity<JsonObject<ProductSpecificationVo>> findBySpecCode(@PathVariable String code){
        Optional<ProductSpecification> specification = specificationService.findBySpecCode(code);
        if(specification.isPresent()){
            return ResponseEntity.ok(JsonObject.success(new ProductSpecificationVo(specification.get())));
        }else {
            return ResponseEntity.ok(JsonObject.success(null));
        }
    }

    @PostMapping(value = "find_by_query_condition")
    @ApiOperation(value = "findByQueryCondition",notes = "根据条件查询")
    public ResponseEntity<JsonObject<List<ProductSpecificationVo>>> findByQueryCondition(@RequestBody ProductSpecificationReq req){
        List<ProductSpecificationVo> list = specificationService.findAll(req);
        return ResponseEntity.ok(JsonObject.success(list));
    }

    @PostMapping(value = "invalid_spec/{id}")
    @ApiOperation(value = "invalidSpec",notes = "禁用规格")
    public ResponseEntity<JsonObject<String>> invalidSpec(@PathVariable Long id){
        specificationService.invalidProductSpecification(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "valid_spec/{id}")
    @ApiOperation(value = "validSpec",notes = "禁用规格")
    public ResponseEntity<JsonObject<String>> validSpec(@PathVariable Long id){
        specificationService.validProductSpecification(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "online_spec/{id}")
    @ApiOperation(value = "onlineSpec",notes = "上线规格")
    public ResponseEntity<JsonObject<String>> onlineSpec(@PathVariable Long id){
        specificationService.onlineProductSpecification(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "offline_spec/{id}")
    @ApiOperation(value = "offlineSpec",notes = "下线规格")
    public ResponseEntity<JsonObject<String>> offlineSpec(@PathVariable Long id){
        specificationService.offLineProductSpecification(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "find_spec_summary_by_page")
    @ApiOperation(value = "findSpecSummaryByPage",notes = "分页查询规格信息")
    public ResponseEntity<JsonObject<Page<StoreSpecificationSummaryVo>>> findSpecSummaryByPage(@RequestBody PageRequestWrapper<StoreSummaryQuery> requestWrapper){
        Page<StoreSpecificationSummaryVo> page = storeSpecificationSummaryService.findByPage(requestWrapper);
        return ResponseEntity.ok(JsonObject.success(page));
    }

    @GetMapping(value = "get_spec_by_online_type/{type}")
    @ApiOperation(value = "getSpecByOnlineType")
    public ResponseEntity<JsonObject<List<SpecDictVo>>> getSpecByOnlineType(@PathVariable Integer type){
        List<SpecDictVo> list = specificationService.findAllOnlineSpec(ValidStatus.of(type).orElse(ValidStatus.VALID));
        return ResponseEntity.ok(JsonObject.success(list));
    }
    @GetMapping(value = "get_spec_by_store_restrict/{id}")
    @ApiOperation(value = "findByStoreRestrict")
    public ResponseEntity<JsonObject<List<SpecDictVo>>> findByStoreRestrict(@PathVariable Long id){
        List<SpecDictVo> list = specificationService.findByStoreRestrict(id);
        return ResponseEntity.ok(JsonObject.success(list));
    }


    @Data
    static class AgentSpecReq{
        private Long agentId;
        private Long specId;
    }
}
