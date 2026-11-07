package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.query.ApplyPayRequest;
import com.worldpay.service.ApplyPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/apply-pay")
@Tag(name = "ApplyPay")
public class ApplyPayController {

    @Autowired
    private ApplyPayService applyPayService;

    @GetMapping
    @Operation(summary = "创建支付")
    public ResponseBase createPayment(@RequestAttribute ApplyPayRequest entity) {
        return applyPayService.createPayment(entity);
    }
}
