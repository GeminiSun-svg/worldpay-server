package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysBrokerageRatesEntity;
import com.worldpay.service.SysBrokerageRatesService;
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
@RequestMapping("/card/api/sys-brokerage-rates")
@Tag(name = "SysBrokerageRates")
public class SysBrokerageRatesController {

    @Autowired
    private SysBrokerageRatesService sysBrokerageRatesService;

    @PostMapping("/all")
    @Operation(summary = "查询系统佣金费率列表")
    public ResponseBase findAll(SysBrokerageRatesEntity entity) {
        return sysBrokerageRatesService.findAll(entity);
    }

    @PostMapping
    @Operation(summary = "新增系统佣金费率")
    public ResponseBase add(@RequestBody SysBrokerageRatesEntity entity) {
        return sysBrokerageRatesService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新系统佣金费率")
    public ResponseBase update(@RequestBody SysBrokerageRatesEntity entity) {
        return sysBrokerageRatesService.update(entity);
    }
}
