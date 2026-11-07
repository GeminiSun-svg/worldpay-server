package com.worldpay.service;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.worldpay.api.request.RechargeCardEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.card.CardShippingEntity;
import com.worldpay.query.CardApplyQueryEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardService {

	ResponseBase pullList();

	ResponseBase findList(CardEntity entity);

	ResponseBase findListPag(CardEntity entity);

	ResponseBase findById(String uuid);

	ResponseBase update(CardEntity entity);
	
//	@PostMapping("/openCardApplyV1")
//	@Operation(summary = "")

//	ResponseBase openCardApply(CardApplyQueryEntity entity);
//	
//	@PostMapping("/topUpV1")
//	@Operation(summary = "")

//	ResponseBase topUp(RechargeCardEntity entity);

	ResponseBase openCardApplyV2(CardApplyQueryEntity entity);

	ResponseBase topUpV2(RechargeCardEntity entity);

	ResponseBase cardBinding(Integer applyId,String cardNumber,String pinNum);

	ResponseBase shipping(CardShippingEntity entity,HttpServletRequest request);

	ResponseBase upLogisticsNum(Integer applyId,String logisticsNum);

	ResponseBase findLogistics(String logisticsNum,Integer applyId);

	ResponseBase updateImg(String uuid,MultipartFile file);

	ResponseBase updateListImg(String uuid,MultipartFile file);

	ResponseBase copyCard(String uuid);

	ResponseBase bankTopUpSend(String uid,Integer type);

	ResponseBase setPin(String uid,Integer userBankcardId,String pinNum);

	ResponseBase findOpenCardFee(String uid,String cardUuid);

	ResponseBase checkFirstTopUp(String uid,Integer userBankcardId,String cardUUid);

	ResponseBase upState(String uuid,Integer stateId);
}
