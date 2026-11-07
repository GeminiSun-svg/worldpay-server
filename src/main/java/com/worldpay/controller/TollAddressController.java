package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.TollAddressEntity;
import com.worldpay.service.TollAddressService;
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
@RequestMapping("/card/api/toll-address")
@Tag(name = "TollAddress")
public class TollAddressController {

    @Autowired
    private TollAddressService tollAddressService;

    @GetMapping("/all")
    @Operation(summary = "查询收费地址列表")
    public ResponseBase findAll() {
        return tollAddressService.findAll();
    }

    @PostMapping
    @Operation(summary = "新增收费地址")
    public ResponseBase add(TollAddressEntity entity) {
        return tollAddressService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新收费地址")
    public ResponseBase update(TollAddressEntity entity) {
        return tollAddressService.update(entity);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询收费地址列表")
    public ResponseBase findList(TollAddressEntity entity) {
        return tollAddressService.findList(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除收费地址")
    public ResponseBase delete(Integer id) {
        return tollAddressService.delete(id);
    }
}
