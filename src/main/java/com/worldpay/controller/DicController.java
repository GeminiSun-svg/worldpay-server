package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.service.DicService;
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
@RequestMapping("/card/api/dic")
@Tag(name = "Dic")
public class DicController {

    @Autowired
    private DicService dicService;

    @GetMapping("/language")
    @Operation(summary = "查询语言列表")
    public ResponseBase getLanguage() {
        return dicService.getLanguage();
    }

    @GetMapping("/country-code")
    @Operation(summary = "查询国家区号")
    public ResponseBase getCountryCode() {
        return dicService.getCountryCode();
    }

    @GetMapping("/country")
    @Operation(summary = "查询国家/地区列表")
    public ResponseBase findCountry(Integer parentId) {
        return dicService.findCountry(parentId);
    }

    @GetMapping("/card-state")
    @Operation(summary = "查询国家/地区列表")
    public ResponseBase findCountry() {
        return dicService.findCountry();
    }

    @GetMapping("/pay-type")
    @Operation(summary = "查询支付类型")
    public ResponseBase findPayType() {
        return dicService.findPayType();
    }

    @GetMapping("/kyc-state")
    @Operation(summary = "查询 KYC 认证状态")
    public ResponseBase findKycState() {
        return dicService.findKycState();
    }

    @GetMapping("/user-state")
    @Operation(summary = "查询用户状态")
    public ResponseBase findUserState() {
        return dicService.findUserState();
    }

    @GetMapping("/protocol-type")
    @Operation(summary = "查询协议类型")
    public ResponseBase findProtocolType() {
        return dicService.findProtocolType();
    }

    @GetMapping("/jump-address")
    @Operation(summary = "查询跳转地址")
    public ResponseBase findJumpAddress() {
        return dicService.findJumpAddress();
    }

    @GetMapping("/sys-config-type")
    @Operation(summary = "查询系统配置类型")
    public ResponseBase findSysConfigType() {
        return dicService.findSysConfigType();
    }

    @GetMapping("/netwok-list")
    @Operation(summary = "查询充值网络列表")
    public ResponseBase findNetwokList() {
        return dicService.findNetwokList();
    }

    @GetMapping("/get-netwok-list")
    @Operation(summary = "查询充值网络列表")
    public ResponseBase getNetwokList() {
        return dicService.getNetwokList();
    }

    @GetMapping("/record-state")
    @Operation(summary = "查询记录状态")
    public ResponseBase findRecordState() {
        return dicService.findRecordState();
    }

    @GetMapping("/wallet-operation-type")
    @Operation(summary = "获取钱包操作类型")
    public ResponseBase getWalletOperationType() {
        return dicService.getWalletOperationType();
    }
}
