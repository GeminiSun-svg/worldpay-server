package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletTransfetRatesEntity;
import com.worldpay.service.WalletTransfetRatesService;
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
@RequestMapping("/card/api/wallet-transfet-rates")
@Tag(name = "WalletTransfetRates")
public class WalletTransfetRatesController {

    @Autowired
    private WalletTransfetRatesService walletTransfetRatesService;

    @GetMapping("/list")
    @Operation(summary = "条件查询钱包转账费率列表")
    public ResponseBase findList() {
        return walletTransfetRatesService.findList();
    }

    @PostMapping
    @Operation(summary = "新增钱包转账费率")
    public ResponseBase add(@RequestBody WalletTransfetRatesEntity entity) {
        return walletTransfetRatesService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新钱包转账费率")
    public ResponseBase update(@RequestBody WalletTransfetRatesEntity entity) {
        return walletTransfetRatesService.update(entity);
    }
}
