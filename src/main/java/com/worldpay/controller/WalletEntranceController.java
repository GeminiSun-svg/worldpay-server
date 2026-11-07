package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.service.WalletEntranceService;
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
@RequestMapping("/card/api/wallet-entrance")
@Tag(name = "WalletEntrance")
public class WalletEntranceController {

    @Autowired
    private WalletEntranceService walletEntranceService;

    @PostMapping("/list")
    @Operation(summary = "条件查询钱包入口列表")
    public ResponseBase findList() {
        return walletEntranceService.findList();
    }

    @GetMapping("/operate")
    @Operation(summary = "钱包入口开关操作")
    public ResponseBase operate(Boolean type,String userName) {
        return walletEntranceService.operate(type, userName);
    }
}
