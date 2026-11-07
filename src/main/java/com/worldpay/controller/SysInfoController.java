package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysInfoEntity;
import com.worldpay.service.SysInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/card/api/info")
@Tag(name = "SysInfo")
public class SysInfoController {

    @Autowired
    private SysInfoService sysInfoService;

    @PostMapping("/all")
    @Operation(summary = "查询系统信息 / 配置列表")
    public ResponseBase findAll(@RequestBody SysInfoEntity entity) {
        return sysInfoService.findAll(entity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询系统信息 / 配置详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return sysInfoService.findById(id);
    }

    @PostMapping
    @Operation(summary = "新增系统信息 / 配置")
    public ResponseBase add(@RequestBody SysInfoEntity entity) {
        return sysInfoService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新系统信息 / 配置")
    public ResponseBase update(@RequestBody SysInfoEntity entity) {
        return sysInfoService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除系统信息 / 配置")
    public ResponseBase delete(Integer id) {
        return sysInfoService.delete(id);
    }

    @GetMapping("/change-status")
    @Operation(summary = "变更状态")
    public ResponseBase changeStatus(Integer id,Integer statusType) {
        return sysInfoService.changeStatus(id, statusType);
    }

    @GetMapping("/config-info")
    @Operation(summary = "查询配置信息")
    public ResponseBase findConfigInfo(Integer configType) {
        return sysInfoService.findConfigInfo(configType);
    }
}
