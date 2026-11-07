package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.product.ProductEntity;
import com.worldpay.entity.product.ProductOrderEntity;
import com.worldpay.query.ProductPayEntity;
import com.worldpay.service.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/card/api/product")
@Tag(name = "Product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "新增产品")
    public ResponseBase add(String dataStr,MultipartFile file) {
        return productService.add(dataStr, file);
    }

    @PutMapping
    @Operation(summary = "更新产品")
    public ResponseBase update(String dataStr,MultipartFile file) {
        return productService.update(dataStr, file);
    }

    @GetMapping("/on-and-off")
    @Operation(summary = "产品上下架")
    public ResponseBase OnAndOff(Integer id,Integer type,String language) {
        return productService.OnAndOff(id, type, language);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询产品列表")
    public ResponseBase findList(@RequestBody ProductEntity entity) {
        return productService.findList(entity);
    }

    @PostMapping("/pc-find-list")
    @Operation(summary = "后台查询产品列表")
    public ResponseBase pcFindList(@RequestBody ProductEntity entity) {
        return productService.pcFindList(entity);
    }

    @PostMapping("/pay")
    @Operation(summary = "产品支付")
    public ResponseBase pay(@RequestBody ProductPayEntity entity) {
        return productService.pay(entity);
    }

    @GetMapping("/user-assets")
    @Operation(summary = "查询用户资产")
    public ResponseBase userAssets(String uid) {
        return productService.userAssets(uid);
    }

    @PostMapping("/order-list")
    @Operation(summary = "查询订单列表")
    public ResponseBase orderList(@RequestBody ProductOrderEntity entity) {
        return productService.orderList(entity);
    }

    @GetMapping("/order-finish")
    @Operation(summary = "完成订单")
    public ResponseBase orderFinish(Integer id) {
        return productService.orderFinish(id);
    }

    @DeleteMapping
    @Operation(summary = "删除产品")
    public ResponseBase delete(Integer id) {
        return productService.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询产品详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return productService.findById(id);
    }
}
