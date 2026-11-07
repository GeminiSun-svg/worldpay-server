package com.worldpay.controller;

import com.worldpay.api.request.UserIntiterEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.service.AppUserIntiterService;
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

@RestController
@CrossOrigin
@RequestMapping("/card/api/app-user-intiter")
@Tag(name = "AppUserIntiter")
public class AppUserIntiterController {

    @Autowired
    private AppUserIntiterService appUserIntiterService;

    @PostMapping("/all")
    @Operation(summary = "查询App 用户邀请列表")
    public ResponseBase findAll(AppUserIntiterEntity entity, HttpServletRequest request) {
        return appUserIntiterService.findAll(entity, request);
    }

    @GetMapping("/channel-invitation-code-by-user-id/{userId}")
    @Operation(summary = "查询渠道邀请验证码按用户ID")
    public ResponseBase findChannelInvitationCodeByUserId(@PathVariable Integer userId) {
        return appUserIntiterService.findChannelInvitationCodeByUserId(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询App 用户邀请详情")
    public ResponseBase findById(@PathVariable Integer id) {
        return appUserIntiterService.findById(id);
    }

    @PostMapping("/app-user-intiter")
    @Operation(summary = "新增App用户邀请")
    public ResponseBase insertAppUserIntiter(UserIntiterEntity userIntiterEntity) {
        return appUserIntiterService.insertAppUserIntiter(userIntiterEntity);
    }
}
