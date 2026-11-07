package com.worldpay.controller;

import com.worldpay.api.request.IosPayReqEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.goods.IosGoodsEntity;
import com.worldpay.service.IosGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/ios-goods")
@Tag(name = "IosGoods")
public class IosGoodsController {

    @Autowired
    private IosGoodsService iosGoodsService;

    @GetMapping("/all")
    @Operation(summary = "查询iOS 商品列表")
    public ResponseBase findAll() {
        return iosGoodsService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询iOS 商品详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return iosGoodsService.findById(id);
    }

    @GetMapping("/by-num")
    @Operation(summary = "查询按编号")
    public ResponseBase findByNum(String num) {
        return iosGoodsService.findByNum(num);
    }

    @PostMapping("/verify")
    @Operation(summary = "验证")
    public ResponseBase verify(@RequestBody IosPayReqEntity entity) {
        return iosGoodsService.verify(entity);
    }

    @PostMapping
    @Operation(summary = "新增iOS 商品")
    public ResponseBase add(@RequestBody IosGoodsEntity entity) {
        return iosGoodsService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新iOS 商品")
    public ResponseBase update(@RequestBody IosGoodsEntity entity) {
        return iosGoodsService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除iOS 商品")
    public ResponseBase delete(Integer id) {
        return iosGoodsService.delete(id);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询iOS 商品列表")
    public ResponseBase findList(@RequestBody IosGoodsEntity entity) {
        return iosGoodsService.findList(entity);
    }
}
