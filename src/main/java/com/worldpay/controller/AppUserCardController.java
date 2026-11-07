package com.worldpay.controller;

import com.worldpay.api.request.CardPinReqEntity;
import com.worldpay.api.request.PublicEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.entity.card.CardCloseEntity;
import com.worldpay.entity.card.CardTransactionEntity;
import com.worldpay.query.UserCardTagEntity;
import com.worldpay.service.AppUserCardService;
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
@RequestMapping("/card/api/app-user-card")
@Tag(name = "AppUserCard")
public class AppUserCardController {

    @Autowired
    private AppUserCardService appUserCardService;

    @GetMapping("/user-card-assets")
    @Operation(summary = "查询用户卡资产")
    public ResponseBase findUserCardAssets(String uid) {
        return appUserCardService.findUserCardAssets(uid);
    }

    @PostMapping("/user-card-list")
    @Operation(summary = "查询用户卡片列表")
    public ResponseBase findUserCardList(@RequestBody AppUserCardEntity entity) {
        return appUserCardService.findUserCardList(entity);
    }

    @PostMapping("/user-card-activation-list")
    @Operation(summary = "查询用户卡激活列表")
    public ResponseBase findUserCardActivationList(@RequestBody AppUserCardEntity entity) {
        return appUserCardService.findUserCardActivationList(entity);
    }

    @PostMapping("/pc-find-user-card-list")
    @Operation(summary = "后台查询用户卡片列表")
    public ResponseBase pcFindUserCardList(@RequestBody AppUserCardEntity entity) {
        return appUserCardService.pcFindUserCardList(entity);
    }

    @GetMapping("/user-card-info")
    @Operation(summary = "查询用户卡片信息")
    public ResponseBase findUserCardInfo(Integer id) {
        return appUserCardService.findUserCardInfo(id);
    }

    @PostMapping("/transaction")
    @Operation(summary = "查询交易")
    public ResponseBase findTransaction(@RequestBody CardTransactionEntity entity) {
        return appUserCardService.findTransaction(entity);
    }

    @PostMapping("/card-cvv")
    @Operation(summary = "查询卡片 CVV")
    public ResponseBase findCardCvv(@RequestBody PublicEntity entity) {
        return appUserCardService.findCardCvv(entity);
    }

    @PostMapping("/card-expiration-time")
    @Operation(summary = "查询卡片有效期")
    public ResponseBase findCardExpirationTime(@RequestBody PublicEntity entity) {
        return appUserCardService.findCardExpirationTime(entity);
    }

    @PostMapping("/close-card")
    @Operation(summary = "注销卡片")
    public ResponseBase closeCard(@RequestBody CardCloseEntity entity) {
        return appUserCardService.closeCard(entity);
    }

    @PostMapping("/up-tag")
    @Operation(summary = "更新卡标签")
    public ResponseBase upTag(@RequestBody UserCardTagEntity entity) {
        return appUserCardService.upTag(entity);
    }

    @PostMapping("/set-pin")
    @Operation(summary = "设置卡片 PIN 码")
    public ResponseBase setPin(@RequestBody CardPinReqEntity entity) {
        return appUserCardService.setPin(entity);
    }

    @GetMapping("/unfreeze")
    @Operation(summary = "解冻")
    public ResponseBase unfreeze(Integer id) {
        return appUserCardService.unfreeze(id);
    }
}
