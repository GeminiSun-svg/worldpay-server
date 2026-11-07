package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletToWebLogEntity;
import com.worldpay.service.WalletToWebLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
@RequestMapping("/card/api/wallet-to-web-log")
@Tag(name = "WalletToWebLog")
public class WalletToWebLogController {

    @Autowired
    private WalletToWebLogService walletToWebLogService;

    @PostMapping
    @Operation(summary = "创建")
    public ResponseBase create(WalletToWebLogEntity entity) {
        return walletToWebLogService.create(entity);
    }

    @GetMapping("/pass")
    @Operation(summary = "站内转账审核通过")
    public ResponseBase pass(Integer id,Integer gooleCode,HttpServletRequest request) {
        return walletToWebLogService.pass(id, gooleCode, request);
    }

    @GetMapping("/reject")
    @Operation(summary = "站内转账审核驳回")
    public ResponseBase reject(Integer id,String rejectContent) {
        return walletToWebLogService.reject(id, rejectContent);
    }

    @GetMapping("/rates")
    @Operation(summary = "查询费率")
    public ResponseBase findRates() {
        return walletToWebLogService.findRates();
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询钱包流水列表")
    public ResponseBase findList(WalletToWebLogEntity entity) {
        return walletToWebLogService.findList(entity);
    }

    @GetMapping("/calculate-amount")
    @Operation(summary = "计算站内转账金额")
    public ResponseBase calculateAmount(BigDecimal amount) {
        return walletToWebLogService.calculateAmount(amount);
    }
}
