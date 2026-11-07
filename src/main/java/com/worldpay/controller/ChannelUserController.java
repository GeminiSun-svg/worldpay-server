package com.worldpay.controller;

import com.worldpay.api.request.ChannelUserAddEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.service.ChannelUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/card/api/channel-user")
@Tag(name = "ChannelUser")
public class ChannelUserController {

    @Autowired
    private ChannelUserService channelUserService;

    @PostMapping("/all")
    @Operation(summary = "查询渠道用户列表")
    public ResponseBase findAll(@RequestBody ChannelUserEntity entity,HttpServletRequest request) {
        return channelUserService.findAll(entity, request);
    }

    @GetMapping("/all")
    @Operation(summary = "App 查询渠道用户列表")
    public ResponseBase appFindAll(ChannelUserEntity entity,HttpServletRequest request) {
        return channelUserService.appFindAll(entity, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "App 查询渠道用户详情")
    public ResponseBase appFindById(@PathVariable String id) {
        return channelUserService.appFindById(id);
    }

    @GetMapping("/channel-user-info")
    @Operation(summary = "查询渠道用户信息")
    public ResponseBase findChannelUserInfo() {
        return channelUserService.findChannelUserInfo();
    }

    @PostMapping
    @Operation(summary = "新增渠道用户")
    public ResponseBase add(@RequestBody ChannelUserAddEntity entity, HttpServletRequest request) {
        return channelUserService.add(entity, request);
    }

    @PutMapping
    @Operation(summary = "更新渠道用户")
    public ResponseBase update(@RequestBody ChannelUserAddEntity entity) {
        return channelUserService.update(entity);
    }

    @PutMapping("/pwd")
    @Operation(summary = "更新渠道用户密码")
    public ResponseBase updatePwd(@RequestBody ChannelUserAddEntity entity) {
        return channelUserService.updatePwd(entity);
    }

    @DeleteMapping("/channel-user")
    @Operation(summary = "删除渠道用户")
    public ResponseBase deleteChannelUser(int userId) {
        return channelUserService.deleteChannelUser(userId);
    }

    @GetMapping("/channel-level")
    @Operation(summary = "查询渠道等级")
    public ResponseBase findChannelLevel(HttpServletRequest request) {
        return channelUserService.findChannelLevel(request);
    }

    @GetMapping("/app-find-channel-level")
    @Operation(summary = "App 查询渠道用户")
    public ResponseBase appFindChannelLevel(HttpServletRequest request) {
        return channelUserService.appFindChannelLevel(request);
    }
}
