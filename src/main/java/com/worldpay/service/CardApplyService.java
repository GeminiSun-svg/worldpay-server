package com.worldpay.service;


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
