package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardSynopsisEntity;
import com.worldpay.service.CardSynopsisService;
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
@RequestMapping("/card/api/card-synopsis")
@Tag(name = "CardSynopsis")
public class CardSynopsisController {

    @Autowired
    private CardSynopsisService cardSynopsisService;

    @PostMapping
    @Operation(summary = "新增卡简介")
    public ResponseBase add(CardSynopsisEntity entity) {
        return cardSynopsisService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新卡简介")
    public ResponseBase update(CardSynopsisEntity entity) {
        return cardSynopsisService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除卡简介")
    public ResponseBase delete(Integer id) {
        return cardSynopsisService.delete(id);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询卡简介列表")
    public ResponseBase findList(CardSynopsisEntity entity) {
        return cardSynopsisService.findList(entity);
    }

    @GetMapping("/all")
    @Operation(summary = "查询卡简介列表")
    public ResponseBase findAll() {
        return cardSynopsisService.findAll();
    }
}
