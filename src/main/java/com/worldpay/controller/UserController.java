package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.query.UpdatePwdEntity;
import com.worldpay.service.UserService;
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
@RequestMapping("/card/api/app-user-sys")
@Tag(name = "User")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/issue-google-secretkey")
    @Operation(summary = "签发谷歌密钥")
    public ResponseBase IssueGoogleSecretkey(String uid) {
        return userService.IssueGoogleSecretkey(uid);
    }

    @GetMapping("/up-google-secretkey")
    @Operation(summary = "更新谷歌密钥")
    public ResponseBase upGoogleSecretkey(String uid,String googleSecretkey) {
        return userService.upGoogleSecretkey(uid, googleSecretkey);
    }

    @GetMapping("/google-secretkey")
    @Operation(summary = "校验谷歌密钥")
    public ResponseBase checkGoogleSecretkey(String uid,String googleCode) {
        return userService.checkGoogleSecretkey(uid, googleCode);
    }

    @PostMapping("/updae-pwd")
    @Operation(summary = "修改登录密码")
    public ResponseBase updaePwd(UpdatePwdEntity entity,HttpServletRequest request) {
        return userService.updaePwd(entity, request);
    }

    @GetMapping("/logout")
    @Operation(summary = "用户退出登录")
    public ResponseBase logout(String uid,HttpServletRequest request) {
        return userService.logout(uid, request);
    }

    @GetMapping("/sign-out")
    @Operation(summary = "退出登录")
    public ResponseBase signOut(HttpServletRequest request) {
        return userService.signOut(request);
    }

    @PostMapping("/list")
    @Operation(summary = "条件查询App 用户（后台管理）列表")
    public ResponseBase findList(AppUserEntity entity) {
        return userService.findList(entity);
    }

    @GetMapping("/up-wallet-state")
    @Operation(summary = "更新钱包状态")
    public ResponseBase upWalletState(Integer id,Boolean state) {
        return userService.upWalletState(id, state);
    }

    @GetMapping("/kyc-file")
    @Operation(summary = "查询KYC文件")
    public ResponseBase findKycFile(String uid) {
        return userService.findKycFile(uid);
    }

    @GetMapping("/forget-passwrod")
    @Operation(summary = "忘记密码（重置密码）")
    public ResponseBase forgetPasswrod(String userEmail,String passwrod,String emailCode) {
        return userService.forgetPasswrod(userEmail, passwrod, emailCode);
    }

    @PostMapping("/up-pay-passwrod")
    @Operation(summary = "修改支付密码")
    public ResponseBase upPayPasswrod(UpdatePwdEntity entity,HttpServletRequest request) {
        return userService.upPayPasswrod(entity, request);
    }
}
