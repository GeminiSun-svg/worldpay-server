package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.query.RejectApplyEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardApplyService {

	ResponseBase openCardApply(CardApplyEntity entity);

	ResponseBase openCardApplyInfo(Integer id);

	ResponseBase openCard(Integer id);

	ResponseBase reject(RejectApplyEntity entity);

	ResponseBase rejectV2(RejectApplyEntity entity);

	ResponseBase openCardV2(Integer id);
	
}
