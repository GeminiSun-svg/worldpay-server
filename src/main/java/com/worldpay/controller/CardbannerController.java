package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardBannerEntity;
import com.worldpay.service.CardbannerService;
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
@RequestMapping("/card/api/card-banner")
@Tag(name = "Cardbanner")
public class CardbannerController {

    @Autowired
    private CardbannerService cardbannerService;

    @PostMapping("/all")
    @Operation(summary = "查询卡 Banner列表")
    public ResponseBase findAll(@RequestBody CardBannerEntity entity) {
        return cardbannerService.findAll(entity);
    }

    @PostMapping
    @Operation(summary = "新增卡 Banner")
    public ResponseBase add(String dataStr,MultipartFile file) {
        return cardbannerService.add(dataStr, file);
    }

    @PutMapping
    @Operation(summary = "更新卡 Banner")
    public ResponseBase update(String dataStr,MultipartFile file) {
        return cardbannerService.update(dataStr, file);
    }

    @DeleteMapping
    @Operation(summary = "删除卡 Banner")
    public ResponseBase delete(Integer id) {
        return cardbannerService.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询卡 Banner详情")
    public ResponseBase findById(@PathVariable String id) {
        return cardbannerService.findById(id);
    }

    @GetMapping("/change-status")
    @Operation(summary = "变更状态")
    public ResponseBase changeStatus(Integer id,Integer statusType) {
        return cardbannerService.changeStatus(id, statusType);
    }

    @GetMapping("/normal-info")
    @Operation(summary = "查询正常信息")
    public ResponseBase findNormalInfo() {
        return cardbannerService.findNormalInfo();
    }
}
