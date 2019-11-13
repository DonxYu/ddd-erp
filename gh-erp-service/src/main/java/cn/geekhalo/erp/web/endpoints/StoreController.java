package cn.geekhalo.erp.web.endpoints;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.model.JsonObject;
import cn.geekhalo.common.validator.BaseValidator;
import cn.geekhalo.common.validator.Validate;
import cn.geekhalo.erp.domain.storehouse.StoreVo;
import cn.geekhalo.erp.domain.storehouse.vo.StoreTypeVo;
import cn.geekhalo.erp.dto.storehouse.StoreCreateDto;
import cn.geekhalo.erp.dto.storehouse.StoreQueryReq;
import cn.geekhalo.erp.dto.storehouse.StoreTypeCreateDto;
import cn.geekhalo.erp.service.storehouse.IStoreService;
import cn.geekhalo.erp.service.storehouse.IStoreTypeService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/store")
@Api(tags = "Store")
public class StoreController {

    @Autowired
    private IStoreService storeService;

    @Autowired
    private IStoreTypeService storeTypeService;


    @PostMapping(value = "create_store_type")
    @ApiOperation(value = "createStoreTye" ,notes = "创建仓库种类")
    public ResponseEntity<JsonObject<String>> createStore(@RequestBody StoreTypeCreateDto dto){
        storeTypeService.createStoreType(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "valid_store_type/{id}")
    @ApiOperation(value = "validStoreType",notes = "启动仓库种类")
    public ResponseEntity<JsonObject<String>> validStoreType(@PathVariable Long id){
        storeTypeService.valid(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "invalid_store_type/{id}")
    @ApiOperation(value = "invalidStoreType",notes = "禁用仓库种类")
    public ResponseEntity<JsonObject<String>> invalidStoreType(@PathVariable Long id){
        storeTypeService.invalid(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @GetMapping(value = "find_all")
    @ApiOperation(value = "findAllTypes")
    public ResponseEntity<JsonObject<List<StoreTypeVo>>> findAllTypes(){
       return ResponseEntity.ok(JsonObject.success(storeTypeService.findAll()));
    }

    @GetMapping(value = "find_select_list")
    @ApiOperation(value = "findSelectList")
    public ResponseEntity<JsonObject<List<StoreTypeVo>>> findSelectList(){
        return ResponseEntity.ok(JsonObject.success(storeTypeService.findAllStoreTypeList()));
    }

    @PostMapping(value = "create_store")
    @ApiOperation(value = "createStore" ,notes = "创建仓库")
    public ResponseEntity<JsonObject<String>> createStore(@RequestBody StoreCreateDto dto){
    	Validate validate =  BaseValidator.verifyDto(dto);
        if(!validate.pass()){
            throw new BusinessException(CodeEnum.ValidateError, JSON.toJSONString(validate.errors()));
        }
        storeService.createStore(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }


    @PostMapping(value = "valid_store/{id}")
    @ApiOperation(value = "validStore",notes = "启动仓库")
    public ResponseEntity<JsonObject<String>> validStore(@PathVariable Long id){
        storeService.validStore(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "invalid_store/{id}")
    @ApiOperation(value = "invalidStore",notes = "禁用仓库")
    public ResponseEntity<JsonObject<String>> invalidStore(@PathVariable Long id){
        storeService.invalidStore(id);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @GetMapping(value = "find_all_store")
    @ApiOperation(value = "findAllStore",notes = "查询所有的store")
    public ResponseEntity<JsonObject<List<StoreVo>>> findAllStore(){
        List<StoreVo> list =  storeService.findAllStore().stream().map(store -> new StoreVo(store)).collect(Collectors.toList());
        return ResponseEntity.ok(JsonObject.success(list));
    }

    @PostMapping(value = "find_by_page")
    @ApiOperation(value = "findByPage",notes = "分页查询Store")
    public ResponseEntity<JsonObject<Page<StoreVo>>> findByPage(@RequestBody PageRequestWrapper<StoreQueryReq> req){
        Page<StoreVo> page = storeService.findByPage(req);
        return ResponseEntity.ok(JsonObject.success(page));
    }
}
