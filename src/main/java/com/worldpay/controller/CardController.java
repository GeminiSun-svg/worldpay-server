package com.worldpay.controller;

import com.worldpay.api.request.RechargeCardEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.card.CardShippingEntity;
import com.worldpay.query.CardApplyQueryEntity;
import com.worldpay.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/card/api/card")
@Tag(name = "Card")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/pull-list")
    @Operation(summary = "拉取卡片列表（三方同步）")
    public ResponseBase pullList() {
        return cardService.pullList();
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询卡片列表")
    public ResponseBase findList(CardEntity entity) {
        return cardService.findList(entity);
    }

    @PostMapping("/list-pag")
    @Operation(summary = "分页查询")
    public ResponseBase findListPag(CardEntity entity) {
        return cardService.findListPag(entity);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "查询卡片详情")
    public ResponseBase findById(@PathVariable String uuid) {
        return cardService.findById(uuid);
    }

    @PutMapping
    @Operation(summary = "更新卡片")
    public ResponseBase update(CardEntity entity) {
        return cardService.update(entity);
    }

    @PostMapping("/open-card-apply")
    @Operation(summary = "开通卡片申请（V2）")
    public ResponseBase openCardApplyV2(CardApplyQueryEntity entity) {
        return cardService.openCardApplyV2(entity);
    }

    @PostMapping("/top-up")
    @Operation(summary = "卡片充值（V2）")
    public ResponseBase topUpV2(RechargeCardEntity entity) {
        return cardService.topUpV2(entity);
    }

    @GetMapping("/card-binding")
    @Operation(summary = "卡片绑定")
    public ResponseBase cardBinding(Integer applyId,String cardNumber,String pinNum) {
        return cardService.cardBinding(applyId, cardNumber, pinNum);
    }

    @PostMapping("/shipping")
    @Operation(summary = "卡片邮寄")
    public ResponseBase shipping(CardShippingEntity entity,HttpServletRequest request) {
        return cardService.shipping(entity, request);
    }

    @GetMapping("/up-logistics-num")
    @Operation(summary = "更新物流单号")
    public ResponseBase upLogisticsNum(Integer applyId,String logisticsNum) {
        return cardService.upLogisticsNum(applyId, logisticsNum);
    }

    @GetMapping("/logistics")
    @Operation(summary = "查询卡片物流信息")
    public ResponseBase findLogistics(String logisticsNum,Integer applyId) {
        return cardService.findLogistics(logisticsNum, applyId);
    }

    @PutMapping("/img")
    @Operation(summary = "更新卡片图片")
    public ResponseBase updateImg(String uuid,MultipartFile file) {
        return cardService.updateImg(uuid, file);
    }

    @PutMapping("/list-img")
    @Operation(summary = "更新卡片列表图片")
    public ResponseBase updateListImg(String uuid,MultipartFile file) {
        return cardService.updateListImg(uuid, file);
    }

    @GetMapping("/copy-card")
    @Operation(summary = "复制卡片信息")
    public ResponseBase copyCard(String uuid) {
        return cardService.copyCard(uuid);
    }

    @GetMapping("/bank-top-up-send")
    @Operation(summary = "发送银行充值验证码")
    public ResponseBase bankTopUpSend(String uid,Integer type) {
        return cardService.bankTopUpSend(uid, type);
    }

    @GetMapping("/set-pin")
    @Operation(summary = "设置卡片 PIN 码")
    public ResponseBase setPin(String uid,Integer userBankcardId,String pinNum) {
        return cardService.setPin(uid, userBankcardId, pinNum);
    }

    @GetMapping("/open-card-fee")
    @Operation(summary = "查询开通卡片手续费")
    public ResponseBase findOpenCardFee(String uid,String cardUuid) {
        return cardService.findOpenCardFee(uid, cardUuid);
    }

    @GetMapping("/first-top-up")
    @Operation(summary = "校验是否首次充值")
    public ResponseBase checkFirstTopUp(String uid,Integer userBankcardId,String cardUUid) {
        return cardService.checkFirstTopUp(uid, userBankcardId, cardUUid);
    }

    @GetMapping("/up-state")
    @Operation(summary = "更新卡片状态")
    public ResponseBase upState(String uuid,Integer stateId) {
        return cardService.upState(uuid, stateId);
    }
}
