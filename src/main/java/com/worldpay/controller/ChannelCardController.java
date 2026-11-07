package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.channel.ChannelCardEntity;
import com.worldpay.service.ChannelCardService;
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
@RequestMapping("/card/api/channel-card")
@Tag(name = "ChannelCard")
public class ChannelCardController {

    @Autowired
    private ChannelCardService channelCardService;

    @PostMapping("/all")
    @Operation(summary = "查询渠道卡列表")
    public ResponseBase findAll(@RequestBody ChannelCardEntity entity) {
        return channelCardService.findAll(entity);
    }

    @GetMapping("/all")
    @Operation(summary = "App 查询渠道卡列表")
    public ResponseBase appFindAll(ChannelCardEntity entity, HttpServletRequest request) {
        return channelCardService.appFindAll(entity, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询渠道卡详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return channelCardService.findById(id);
    }

    @PutMapping
    @Operation(summary = "更新渠道卡")
    public ResponseBase update(@RequestBody ChannelCardEntity entity) {
        return channelCardService.update(entity);
    }

    @GetMapping("/one-click-update")
    @Operation(summary = "一键更新渠道卡")
    public ResponseBase oneClickUpdate() {
        return channelCardService.oneClickUpdate();
    }
}
