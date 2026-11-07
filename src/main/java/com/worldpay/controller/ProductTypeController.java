package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.product.ProductTypeEntity;
import com.worldpay.service.ProductTypeService;
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
@RequestMapping("/card/api/product-type")
@Tag(name = "ProductType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @PostMapping
    @Operation(summary = "新增产品类型")
    public ResponseBase add(ProductTypeEntity entity) {
        return productTypeService.add(entity);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询产品类型列表")
    public ResponseBase findList(ProductTypeEntity entity) {
        return productTypeService.findList(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除产品类型")
    public ResponseBase delete(Integer id) {
        return productTypeService.delete(id);
    }

    @GetMapping("/all")
    @Operation(summary = "查询产品类型列表")
    public ResponseBase findAll() {
        return productTypeService.findAll();
    }

    @GetMapping("/pc-find-all")
    @Operation(summary = "后台查询产品类型列表")
    public ResponseBase pcFindAll(String language) {
        return productTypeService.pcFindAll(language);
    }
}
