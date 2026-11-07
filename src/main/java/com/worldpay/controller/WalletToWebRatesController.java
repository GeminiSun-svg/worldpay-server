package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletToWebRatesEntity;
import com.worldpay.service.WalletToWebRatesService;
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
@RequestMapping("/card/api/withdrawal-rates")
@Tag(name = "WalletToWebRates")
public class WalletToWebRatesController {

    @Autowired
    private WalletToWebRatesService walletToWebRatesService;

    @GetMapping
    @Operation(summary = "查询提现费率")
    public ResponseBase find() {
        return walletToWebRatesService.find();
    }

    @PutMapping
    @Operation(summary = "更新提现费率")
    public ResponseBase update(@RequestBody WalletToWebRatesEntity entity) {
        return walletToWebRatesService.update(entity);
    }
}
