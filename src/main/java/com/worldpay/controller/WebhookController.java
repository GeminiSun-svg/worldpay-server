package com.worldpay.controller;

import com.worldpay.api.request.ResetAddressEntity;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.service.WebhookService;
import com.worldpay.webhook.response.CardRechargeResult;
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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/webhook")
@Tag(name = "Webhook")
public class WebhookController {

    @Autowired
    private WebhookService webhookService;

    @PostMapping("/agent-notify")
    @Operation(summary = "代理回调通知")
    public ApiResponseEntity agentNotify(@RequestBody CardRechargeResult entity) {
        return webhookService.agentNotify(entity);
    }

    @PostMapping("/reset-addres")
    @Operation(summary = "重置地址回调")
    public ResponseBase resetAddres(@RequestBody @Valid ResetAddressEntity entity, HttpServletRequest req) {
        return webhookService.resetAddres(entity, req);
    }

    @GetMapping("/info")
    @Operation(summary = "Webhook 信息接收")
    public ResponseBase info(String uid, Integer userBankcardId) {
        return webhookService.info(uid, userBankcardId);
    }

    @GetMapping("/state")
    @Operation(summary = "统计e")
    public ResponseBase state(String uid, Integer userBankcardId) {
        return webhookService.state(uid, userBankcardId);
    }
}
