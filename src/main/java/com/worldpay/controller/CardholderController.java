package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserHolderEntity;
import com.worldpay.service.CardholderService;
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
@RequestMapping("/card/api/cardholder")
@Tag(name = "Cardholder")
public class CardholderController {

    @Autowired
    private CardholderService cardholderService;

    @PostMapping
    @Operation(summary = "新增持卡人")
    public ResponseBase add(AppUserHolderEntity entity) {
        return cardholderService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新持卡人")
    public ResponseBase update(AppUserHolderEntity entity) {
        return cardholderService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除持卡人")
    public ResponseBase delete(Integer id) {
        return cardholderService.delete(id);
    }

    @GetMapping("/by-uid")
    @Operation(summary = "查询按UID")
    public ResponseBase findByUid(String uid) {
        return cardholderService.findByUid(uid);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询持卡人详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return cardholderService.findById(id);
    }
}
