package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardLableEntity;
import com.worldpay.service.CardLableService;
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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/card-label")
@Tag(name = "CardLable")
public class CardLableController {

    @Autowired
    private CardLableService cardLableService;

    @PostMapping
    @Operation(summary = "新增卡标签")
    public ResponseBase add(@RequestBody @Valid CardLableEntity entity) {
        return cardLableService.add(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除卡标签")
    public ResponseBase delete(Integer id) {
        return cardLableService.delete(id);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询卡标签列表")
    public ResponseBase findList(@RequestBody CardLableEntity entity) {
        return cardLableService.findList(entity);
    }

    @GetMapping("/all")
    @Operation(summary = "查询卡标签列表")
    public ResponseBase findAll() {
        return cardLableService.findAll();
    }
}
