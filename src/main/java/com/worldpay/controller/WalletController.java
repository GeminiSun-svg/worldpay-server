package com.worldpay.controller;

import com.worldpay.api.request.WithdrawalCallbackEntity;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.api.response.UsdtTopinNotifyEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.entity.wallet.WalletManualLogEntity;
import com.worldpay.entity.wallet.WalletTransfetListEntity;
import com.worldpay.query.PublicQueryEntity;
import com.worldpay.service.WalletService;
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

@RestController
@CrossOrigin
@RequestMapping("/card/api/wallet")
@Tag(name = "Wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/topin-usdt-address")
    @Operation(summary = "查询 USDT 充值地址")
    public ResponseBase topinUsdtAddress(String uid) {
        return walletService.topinUsdtAddress(uid);
    }

    @PostMapping("/topin-usdt-notify")
    @Operation(summary = "USDT 链上充值回调")
    public ApiResponseEntity topinUsdtNotify(UsdtTopinNotifyEntity entity,HttpServletRequest req) {
        return walletService.topinUsdtNotify(entity, req);
    }

    @PostMapping("/wallet-log")
    @Operation(summary = "查询钱包日志")
    public ResponseBase walletLog(WalletLogEntity entity) {
        return walletService.walletLog(entity);
    }

    @PostMapping("/wallet-log-stat")
    @Operation(summary = "钱包日志统计")
    public ResponseBase walletLogStat(PublicQueryEntity entity) {
        return walletService.walletLogStat(entity);
    }

    @PostMapping("/wallet-top-up")
    @Operation(summary = "钱包充值")
    public ResponseBase walletTopUp(WalletManualLogEntity entity,HttpServletRequest req) {
        return walletService.walletTopUp(entity, req);
    }

    @PostMapping("/find-wallet-log")
    @Operation(summary = "查询钱包日志")
    public ResponseBase findWalletLog(WalletLogEntity entity) {
        return walletService.findWalletLog(entity);
    }

    @PostMapping("/transfer")
    @Operation(summary = "钱包转账")
    public ResponseBase transfer(WalletTransfetListEntity entity) {
        return walletService.transfer(entity);
    }

    @GetMapping("/transfer-reading")
    @Operation(summary = "查询转账流水")
    public ResponseBase transferReading(Double sendMoney,String uid) {
        return walletService.transferReading(sendMoney, uid);
    }

    @GetMapping("/reading")
    @Operation(summary = "查询钱包流水")
    public ResponseBase findReading() {
        return walletService.findReading();
    }

    @PostMapping("/withdrawal-notify")
    @Operation(summary = "提现回调")
    public ApiResponseEntity withdrawalNotify(WithdrawalCallbackEntity entity) {
        return walletService.withdrawalNotify(entity);
    }
}
