package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.EmailTemplateEntity;
import com.worldpay.service.EmailTemplateService;
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
@RequestMapping("/card/api/email-template")
@Tag(name = "EmailTemplate")
public class EmailTemplateController {

    @Autowired
    private EmailTemplateService emailTemplateService;

    @PostMapping
    @Operation(summary = "新增邮件模板")
    public ResponseBase add(@RequestBody EmailTemplateEntity entity) {
        return emailTemplateService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新邮件模板")
    public ResponseBase update(@RequestBody EmailTemplateEntity entity) {
        return emailTemplateService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除邮件模板")
    public ResponseBase delete(Integer id) {
        return emailTemplateService.delete(id);
    }

    @GetMapping("/code")
    @Operation(summary = "查询验证码")
    public EmailTemplateEntity findCode(Integer code) {
        return emailTemplateService.findCode(code);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询邮件模板列表")
    public ResponseBase findList(@RequestBody EmailTemplateEntity entity) {
        return emailTemplateService.findList(entity);
    }
}
