package com.worldpay.service;

import com.worldpay.api.request.UserIntiterEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserIntiterEntity;

import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface AppUserIntiterService {

ResponseBase findAll(AppUserIntiterEntity entity, HttpServletRequest request);

ResponseBase findChannelInvitationCodeByUserId(@PathVariable Integer userId);

ResponseBase findById(@PathVariable Integer id);

ResponseBase insertAppUserIntiter(UserIntiterEntity userIntiterEntity);
}
