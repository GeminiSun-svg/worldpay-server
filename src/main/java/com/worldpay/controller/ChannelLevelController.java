package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.brokerage.ChannelLevelEntity;
import com.worldpay.service.ChannelLevelService;
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
@RequestMapping("/card/api/channel-level")
@Tag(name = "ChannelLevel")
public class ChannelLevelController {

    @Autowired
    private ChannelLevelService channelLevelService;

    @PostMapping("/all")
    @Operation(summary = "查询渠道等级列表")
    public ResponseBase findAll(@RequestBody ChannelLevelEntity entity) {
        return channelLevelService.findAll(entity);
    }

    @PostMapping
    @Operation(summary = "新增渠道等级")
    public ResponseBase add(@RequestBody ChannelLevelEntity entity) {
        return channelLevelService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新渠道等级")
    public ResponseBase update(@RequestBody ChannelLevelEntity entity) {
        return channelLevelService.update(entity);
    }

    @PutMapping("/status")
    @Operation(summary = "更新状态")
    public ResponseBase updateStatus(int id, int applyState) {
        return channelLevelService.updateStatus(id, applyState);
    }

    @DeleteMapping
    @Operation(summary = "删除渠道等级")
    public ResponseBase delete(int id) {
        return channelLevelService.delete(id);
    }
}
