package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.NoticeEntity;
import com.worldpay.service.NoticeService;
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
@RequestMapping("/card/api/notice")
@Tag(name = "Notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/all")
    @Operation(summary = "查询公告列表")
    public ResponseBase findAll(@RequestBody NoticeEntity entity) {
        return noticeService.findAll(entity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询公告详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return noticeService.findById(id);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询公告列表")
    public ResponseBase findList(@RequestBody NoticeEntity entity) {
        return noticeService.findList(entity);
    }

    @PostMapping
    @Operation(summary = "新增公告")
    public ResponseBase add(@RequestBody NoticeEntity entity) {
        return noticeService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新公告")
    public ResponseBase update(@RequestBody NoticeEntity entity) {
        return noticeService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除公告")
    public ResponseBase delete(Integer id) {
        return noticeService.delete(id);
    }

    @GetMapping("/send")
    @Operation(summary = "发送")
    public ResponseBase send(Integer id) {
        return noticeService.send(id);
    }
}
