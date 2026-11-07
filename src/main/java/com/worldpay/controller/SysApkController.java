package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysApkEntity;
import com.worldpay.service.SysApkService;
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
@RequestMapping("/card/api/sys-apk")
@Tag(name = "SysApk")
public class SysApkController {

    @Autowired
    private SysApkService sysApkService;

    @PostMapping
    @Operation(summary = "新增App 版本")
    public ResponseBase add(String dataStr,MultipartFile file) {
        return sysApkService.add(dataStr, file);
    }

    @PutMapping
    @Operation(summary = "更新App 版本")
    public ResponseBase update(String dataStr,MultipartFile file) {
        return sysApkService.update(dataStr, file);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询App 版本列表")
    public ResponseBase findList(@RequestBody SysApkEntity entity) {
        return sysApkService.findList(entity);
    }

    @GetMapping("/release")
    @Operation(summary = "发布新版本")
    public ResponseBase release(Integer id) {
        return sysApkService.release(id);
    }

    @GetMapping("/last-version")
    @Operation(summary = "查询最新 App 版本")
    public ResponseBase findLastVersion() {
        return sysApkService.findLastVersion();
    }
}
