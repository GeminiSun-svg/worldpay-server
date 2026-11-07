package com.worldpay.controller;

import com.worldpay.api.request.AppBindChannelEditEntity;
import com.worldpay.api.request.AppBindChannelReqEntity;
import com.worldpay.api.request.ApplicationRecordReqEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.ApplicationRecordEntity;
import com.worldpay.service.ApplicationRecordService;
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
@RequestMapping("/card/api/application-record")
@Tag(name = "ApplicationRecord")
public class ApplicationRecordController {

    @Autowired
    private ApplicationRecordService applicationRecordService;

    @GetMapping
    @Operation(summary = "查询申请记录")
    public ResponseBase findApplicationRecord(ApplicationRecordEntity entity) {
        return applicationRecordService.findApplicationRecord(entity);
    }

    @PostMapping("/channel")
    @Operation(summary = "App 查询渠道绑定")
    public ResponseBase appFindChannel(@RequestBody AppBindChannelReqEntity appBindChannelReqEntity) {
        return applicationRecordService.appFindChannel(appBindChannelReqEntity);
    }

    @PostMapping("/app-bind-channel")
    @Operation(summary = "App 绑定渠道")
    public ResponseBase appBindChannel(@RequestBody AppBindChannelEditEntity appBindChannelEditEntity) {
        return applicationRecordService.appBindChannel(appBindChannelEditEntity);
    }

    @PostMapping("/app-is-channel")
    @Operation(summary = "App 查询是否渠道用户")
    public ResponseBase appIsChannel(@RequestBody AppBindChannelEditEntity appBindChannelEditEntity) {
        return applicationRecordService.appIsChannel(appBindChannelEditEntity);
    }

    @PutMapping("/status")
    @Operation(summary = "更新状态")
    public ResponseBase updateStatus(@RequestBody ApplicationRecordReqEntity entity) {
        return applicationRecordService.updateStatus(entity);
    }
}
