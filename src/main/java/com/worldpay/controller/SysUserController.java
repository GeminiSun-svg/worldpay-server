package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/card/api/user")
@Tag(name = "SysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/list")
    @Operation(summary = "条件查询系统用户列表")
    public ResponseBase findList(SysUserEntity entity) {
        return sysUserService.findList(entity);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "用户注册（通用版）")
    public ResponseBase signUp(String user,@RequestPart(required = false) MultipartFile faceFile) {
        return sysUserService.signUp(user, faceFile);
    }

    @GetMapping("/token")
    @Operation(summary = "查询当前登录 Token")
    public ResponseBase findToken(HttpServletRequest request) {
        return sysUserService.findToken(request);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户")
    public ResponseBase delUser(@PathVariable Integer userId) {
        return sysUserService.delUser(userId);
    }

    @PutMapping("/state")
    @Operation(summary = "更新用户状态")
    public ResponseBase updateUserState(Integer userId, Integer newState) {
        return sysUserService.updateUserState(userId, newState);
    }

    @PutMapping
    @Operation(summary = "更新用户")
    public ResponseBase updateUser(String user,@RequestPart(required = false) MultipartFile file) {
        return sysUserService.updateUser(user, file);
    }

    @PostMapping("/reset-pwd")
    @Operation(summary = "重置Pwd")
    public ResponseBase resetPwd(Integer userId,String password) {
        return sysUserService.resetPwd(userId, password);
    }

    @PostMapping("/reset-user-pwd")
    @Operation(summary = "重置用户Pwd")
    public ResponseBase resetUserPwd(Integer userId,String password) {
        return sysUserService.resetUserPwd(userId, password);
    }

    @PostMapping("/verify-pwd")
    @Operation(summary = "验证Pwd")
    public ResponseBase verifyPwd(Integer userId,String password) {
        return sysUserService.verifyPwd(userId, password);
    }

    @PutMapping("/device-model")
    @Operation(summary = "更新系统用户")
    public ResponseBase updateDeviceModel(SysUserEntity entity) {
        return sysUserService.updateDeviceModel(entity);
    }

    @GetMapping("/generate-token")
    @Operation(summary = "生成登录 Token")
    public ResponseBase generateToken() {
        return sysUserService.generateToken();
    }

    @GetMapping("/up-google-secretkey")
    @Operation(summary = "更新谷歌密钥")
    public ResponseBase upGoogleSecretkey(Integer userId,String googleSecretkey) {
        return sysUserService.upGoogleSecretkey(userId, googleSecretkey);
    }

    @GetMapping("/issue-google-secretkey")
    @Operation(summary = "签发谷歌密钥")
    public ResponseBase IssueGoogleSecretkey(String userName) {
        return sysUserService.IssueGoogleSecretkey(userName);
    }

    @GetMapping("/password-or-key")
    @Operation(summary = "获取密码OrKey")
    public ResponseBase getPasswordOrKey() {
        return sysUserService.getPasswordOrKey();
    }
}
