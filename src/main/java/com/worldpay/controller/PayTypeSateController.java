package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.PayTypeSateEntity;
import com.worldpay.service.PayTypeSateService;
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
@RequestMapping("/card/api/pay-type-sate")
@Tag(name = "PayTypeSate")
public class PayTypeSateController {

    @Autowired
    private PayTypeSateService payTypeSateService;

    @GetMapping
    @Operation(summary = "查询支付方式列表")
    public ResponseBase findAll() {
        return payTypeSateService.findAll();
    }

    @GetMapping("/manage")
    @Operation(summary = "查询支付方式管理列表")
    public ResponseBase findManage() {
        return payTypeSateService.findManage();
    }

    @PutMapping
    @Operation(summary = "更新支付方式")
    public ResponseBase update(@RequestBody PayTypeSateEntity entity) {
        return payTypeSateService.update(entity);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询支付方式列表")
    public ResponseBase findList(@RequestBody PayTypeSateEntity entity) {
        return payTypeSateService.findList(entity);
    }

    @PostMapping
    @Operation(summary = "新增支付方式")
    public ResponseBase add(@RequestBody PayTypeSateEntity entity) {
        return payTypeSateService.add(entity);
    }
}
