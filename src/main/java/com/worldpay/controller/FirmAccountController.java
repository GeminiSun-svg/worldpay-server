package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.firm.FirmAccountEntity;
import com.worldpay.service.FirmAccountService;
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
@RequestMapping("/card/api/firm-account")
@Tag(name = "FirmAccount")
public class FirmAccountController {

    @Autowired
    private FirmAccountService firmAccountService;

    @PostMapping
    @Operation(summary = "新增公司账户")
    public ResponseBase add(@RequestBody FirmAccountEntity entity) {
        return firmAccountService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新公司账户")
    public ResponseBase update(@RequestBody FirmAccountEntity entity) {
        return firmAccountService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除公司账户")
    public ResponseBase delete(Integer id) {
        return firmAccountService.delete(id);
    }

    @GetMapping
    @Operation(summary = "查询公司账户")
    public ResponseBase find() {
        return firmAccountService.find();
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询公司账户列表")
    public ResponseBase findList(FirmAccountEntity entity) {
        return firmAccountService.findList(entity);
    }
}
