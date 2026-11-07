package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.query.RejectApplyEntity;
import com.worldpay.service.CardApplyService;
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
@RequestMapping("/card/api/card-apply")
@Tag(name = "CardApply")
public class CardApplyController {

    @Autowired
    private CardApplyService cardApplyService;

    @PostMapping("/open-card-apply")
    @Operation(summary = "开通卡片申请")
    public ResponseBase openCardApply(CardApplyEntity entity) {
        return cardApplyService.openCardApply(entity);
    }

    @GetMapping("/open-card-apply-info")
    @Operation(summary = "查询开卡申请信息")
    public ResponseBase openCardApplyInfo(Integer id) {
        return cardApplyService.openCardApplyInfo(id);
    }

    @GetMapping("/open-card")
    @Operation(summary = "开通卡片")
    public ResponseBase openCard(Integer id) {
        return cardApplyService.openCard(id);
    }

    @PostMapping("/reject")
    @Operation(summary = "驳回开卡申请")
    public ResponseBase reject(RejectApplyEntity entity) {
        return cardApplyService.reject(entity);
    }

    @PostMapping("/reject-v2")
    @Operation(summary = "驳回开卡申请（V2）")
    public ResponseBase rejectV2(RejectApplyEntity entity) {
        return cardApplyService.rejectV2(entity);
    }

    @GetMapping("/open-card-v2")
    @Operation(summary = "开通卡片（V2）")
    public ResponseBase openCardV2(Integer id) {
        return cardApplyService.openCardV2(id);
    }
}
