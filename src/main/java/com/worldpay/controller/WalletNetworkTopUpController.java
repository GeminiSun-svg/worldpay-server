package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletNetworkTopUpEntity;
import com.worldpay.service.WalletNetworkTopUpService;
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
@RequestMapping("/card/api/wallet-network-top-up")
@Tag(name = "WalletNetworkTopUp")
public class WalletNetworkTopUpController {

    @Autowired
    private WalletNetworkTopUpService walletNetworkTopUpService;

    @PostMapping
    @Operation(summary = "新增链上充值")
    public ResponseBase add(WalletNetworkTopUpEntity entity) {
        return walletNetworkTopUpService.add(entity);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询链上充值列表")
    public ResponseBase findList(WalletNetworkTopUpEntity entity) {
        return walletNetworkTopUpService.findList(entity);
    }

    @GetMapping("/close")
    @Operation(summary = "关闭链上充值网络")
    public ResponseBase close(Integer id,String reviewRemark) {
        return walletNetworkTopUpService.close(id, reviewRemark);
    }
}
