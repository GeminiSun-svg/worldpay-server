package com.worldpay.service;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.constants.ApiVersion;
import com.worldpay.constants.ClientType;
import com.worldpay.entity.appuser.AppUserEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface AppUserService {

ResponseBase signUp(AppUserEntity entity);

ResponseBase signUpV2(AppUserEntity entity);

ResponseBase login(AppUserEntity entity, HttpServletRequest req);

ResponseBase googleCode(String email);

ResponseBase findToken(HttpServletRequest request);

ResponseBase findTokenV2(HttpServletRequest request);

ResponseBase sendEmailCode(String userEmail);

ResponseBase checkEmailCode(String userEmail, String emailCode);

ResponseBase userinfo(HttpServletRequest request);

ResponseBase checkGoogleSecretkey(String uid, Integer googleCode);

ResponseBase cashAmount(HttpServletRequest request);

ResponseBase withdrawalToWallet(@RequestBody AppUserEntity entity, HttpServletRequest request);

ResponseBase findKycState(String uid);

ResponseBase kycRetry(String uid);

ResponseBase findAddress(String uid);

ResponseBase kycRetryAll();

}
