package cn.geekhalo.erp.web.endpoints;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.ddd.support.PageRequestWrapper;
import cn.geekhalo.common.exception.BusinessException;
import cn.geekhalo.common.model.JsonObject;
import cn.geekhalo.erp.domain.product.vo.GoodsVo;
import cn.geekhalo.erp.dto.product.*;
import cn.geekhalo.erp.service.product.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "v1/goods")
@Api(tags = "Goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @PostMapping("goods_create_store")
    @ApiOperation(value = "goodsCreateInStore",notes = "物品初次录库")
    public ResponseEntity<JsonObject<String>>  goodsCreateInStore(@RequestBody GoodsInStoreDto dto){
        goodsService.goodsCreateInStore(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "goods_make")
    @ApiOperation(value = "goodsMake",notes = "商品制造")
    public ResponseEntity<JsonObject<String>> goodsMake(@RequestBody GoodsMakeDto dto){
        goodsService.goodsMake(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @PostMapping(value = "goods_assemble")
    @ApiOperation(value = "goodsAssemble",notes = "商品配货")
    public ResponseEntity<JsonObject<String>> goodsAssemble(@RequestBody GoodsAssembleDto dto){
        goodsService.goodsAssemble(dto);
        return ResponseEntity.ok(JsonObject.success(CodeEnum.Success.getName()));
    }

    @GetMapping(value = "get_unserialize_bars/{size}")
    @ApiOperation(value = "getUnserializeBars",notes = "获取非序列化的条码")
    public ResponseEntity<JsonObject<List<String>>> getUnserializeBars(@PathVariable Integer size){
        boolean isLegal = GenericValidator.isInRange(size,1,100000);
        if(!isLegal){
            throw new BusinessException(CodeEnum.BusinessError);

        }
        List<String> list = goodsService.generateBarCode(size);
        return ResponseEntity.ok(JsonObject.success(list));
    }

    @GetMapping(value = "get_available_goods_bars")
    @ApiOperation(value = "getAvailableGoodsBars",notes = "获取可用的产品条码")
    public ResponseEntity<JsonObject<List<String>>> getAvailableGoodsBars(@RequestBody AvailableGoodBarDto dto){
        List<String> list = goodsService.getAvailableGoodsBars(dto.getSpecId(),dto.getNumber(),dto.getStoreId());
        return ResponseEntity.ok(JsonObject.success(list));
    }

    @GetMapping(value = "find_goods_children_info/{id}")
    @ApiOperation(value = "findGoodsChildrenInfo",notes = "查询产品子件信息")
    public ResponseEntity<JsonObject<List<GoodsVo>>> findGoodsChildrenInfo(@PathVariable Long id){
        return ResponseEntity.ok(JsonObject.success(goodsService.getGoodsChildrenInfo(id)));
    }

    @PostMapping(value = "find_goods_by_page")
    @ApiOperation(value = "findGoodsByPage")
    public ResponseEntity<JsonObject<Page<GoodsVo>>> findGoodsByPage(@RequestBody PageRequestWrapper<GoodsQuery> requestWrapper){
        Page<GoodsVo> page = goodsService.findByPage(requestWrapper);
        return ResponseEntity.ok(JsonObject.success(page));
    }

    @PostMapping(value = "find_goods_by_code_store")
    @ApiOperation(value = "findGoodsByCodeAndStore")
    public ResponseEntity<JsonObject<GoodsVo>> findGoodsByCodeAndStore(@RequestBody StoreBarCode req){
        Optional<GoodsVo> goodsVo = goodsService.findByStoreIdAndBarcode(req.getStoreId(),req.getBarCode());
        return ResponseEntity.ok(JsonObject.success(goodsVo.get()));
    }

    @Data
    static class StoreBarCode{
        private Long storeId;
        private String barCode;
    }


}
