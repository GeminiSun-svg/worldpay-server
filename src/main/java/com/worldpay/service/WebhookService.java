package com.worldpay.service;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.worldpay.api.request.ResetAddressEntity;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.webhook.response.CardRechargeResult;

import io.swagger.v3.oas.annotations.Operation;

public interface WebhookService {

ApiResponseEntity agentNotify(CardRechargeResult entity);

ResponseBase resetAddres(ResetAddressEntity entity,HttpServletRequest req);

	ResponseBase info(String uid, Integer userBankcardId);

	ResponseBase state(String uid, Integer userBankcardId);
	
}
