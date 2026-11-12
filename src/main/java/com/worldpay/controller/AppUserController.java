package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/card/api/app-user")
@Tag(name = "AppUser")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/sign-up")
    @Operation(summary = "用户注册（通用版）")
    public ResponseBase signUp(@RequestBody AppUserEntity entity) {
        return appUserService.signUpV2(entity);
    }

    @PostMapping
    @Operation(summary = "用户注册（iOS V2 版，含钱包/充值开关）")
    public ResponseBase signUpV2(@RequestBody AppUserEntity entity) {
        return appUserService.signUpV2(entity);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResponseBase login(@RequestBody AppUserEntity entity, HttpServletRequest req) {
        return appUserService.login(entity, req);
    }

    @GetMapping("/google-code")
    @Operation(summary = "获取谷歌验证二维码")
    public ResponseBase googleCode(String email) {
        return appUserService.googleCode(email);
    }

    @GetMapping("/token")
    @Operation(summary = "查询当前登录 Token")
    public ResponseBase findToken(HttpServletRequest request) {
        return appUserService.findTokenV2(request);
    }

    @GetMapping
    @Operation(summary = "查询当前登录 Token（iOS V2）")
    public ResponseBase findTokenV2(HttpServletRequest request) {
        return appUserService.findTokenV2(request);
    }

    @GetMapping("/send-email-code")
    @Operation(summary = "发送邮箱验证码")
    public ResponseBase sendEmailCode(String userEmail) {
        return appUserService.sendEmailCode(userEmail);
    }

    @GetMapping("/email-code")
    @Operation(summary = "校验邮箱验证码")
    public ResponseBase checkEmailCode(String userEmail, String emailCode) {
        return appUserService.checkEmailCode(userEmail, emailCode);
    }

    @GetMapping("/userinfo")
    @Operation(summary = "查询当前用户信息")
    public ResponseBase userinfo(HttpServletRequest request) {
        return appUserService.userinfo(request);
    }

    @GetMapping("/google-secretkey")
    @Operation(summary = "校验谷歌密钥")
    public ResponseBase checkGoogleSecretkey(String uid, Integer googleCode) {
        return appUserService.checkGoogleSecretkey(uid, googleCode);
    }

    @GetMapping("/cash-amount")
    @Operation(summary = "查询可提现金额")
    public ResponseBase cashAmount(HttpServletRequest request) {
        return appUserService.cashAmount(request);
    }

    @PostMapping("/withdrawal-to-wallet")
    @Operation(summary = "提现到钱包")
    public ResponseBase withdrawalToWallet(@RequestBody AppUserEntity entity, HttpServletRequest request) {
        return appUserService.withdrawalToWallet(entity, request);
    }

    @GetMapping("/kyc-state")
    @Operation(summary = "查询 KYC 认证状态")
    public ResponseBase findKycState(String uid) {
        return appUserService.findKycState(uid);
    }

    @GetMapping("/kyc-retry")
    @Operation(summary = "KYC 认证重试")
    public ResponseBase kycRetry(String uid) {
        return appUserService.kycRetry(uid);
    }

    @GetMapping("/address")
    @Operation(summary = "查询用户地址")
    public ResponseBase findAddress(String uid) {
        return appUserService.findAddress(uid);
    }

    @GetMapping("/kyc-retry-all")
    @Operation(summary = "KYC 全部重试（后台）")
    public ResponseBase kycRetryAll() {
        return appUserService.kycRetryAll();
    }
}
