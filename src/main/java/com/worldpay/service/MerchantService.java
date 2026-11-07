package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.api.request.RechargeEntity;
import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface MerchantService {

ResponseBase findInfo();

ResponseBase rechargeList(RechargeEntity entity);

}
