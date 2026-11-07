package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.HelpEntity;
import com.worldpay.service.HelpService;
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
@RequestMapping("/card/api/help")
@Tag(name = "Help")
public class HelpController {

    @Autowired
    private HelpService helpService;

    @PostMapping
    @Operation(summary = "新增帮助中心")
    public ResponseBase add(@RequestBody HelpEntity entity) {
        return helpService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新帮助中心")
    public ResponseBase update(@RequestBody HelpEntity entity) {
        return helpService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除帮助中心")
    public ResponseBase delete(Integer id) {
        return helpService.delete(id);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询帮助中心列表")
    public ResponseBase findList(@RequestBody HelpEntity entity) {
        return helpService.findList(entity);
    }

    @PostMapping("/pc-find-list")
    @Operation(summary = "后台查询帮助中心列表")
    public ResponseBase pcFindList(@RequestBody HelpEntity entity) {
        return helpService.pcFindList(entity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询帮助中心详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return helpService.findById(id);
    }
}
