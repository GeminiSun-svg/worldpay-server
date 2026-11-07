package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.MessageTemplateEntity;
import com.worldpay.service.MessageTemplateService;
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
@RequestMapping("/card/api/message-template")
@Tag(name = "MessageTemplate")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @PostMapping
    @Operation(summary = "新增消息模板")
    public ResponseBase add(MessageTemplateEntity entity) {
        return messageTemplateService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新消息模板")
    public ResponseBase update(MessageTemplateEntity entity) {
        return messageTemplateService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除消息模板")
    public ResponseBase delete(Integer id) {
        return messageTemplateService.delete(id);
    }

    @GetMapping("/code")
    @Operation(summary = "查询验证码")
    public MessageTemplateEntity findCode(Integer code) {
        return messageTemplateService.findCode(code);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询消息模板列表")
    public ResponseBase findList(MessageTemplateEntity entity) {
        return messageTemplateService.findList(entity);
    }
}
