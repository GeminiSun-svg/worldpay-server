package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.api.request.CardPinReqEntity;
import com.worldpay.api.request.PublicEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.entity.card.CardCloseEntity;
import com.worldpay.entity.card.CardTransactionEntity;
import com.worldpay.query.UserCardTagEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface AppUserCardService {

	ResponseBase findUserCardAssets(String uid);

	ResponseBase findUserCardList(AppUserCardEntity entity);

	ResponseBase findUserCardActivationList(AppUserCardEntity entity);
	
//	@PostMapping(value = "/findUserCardList", headers = { ApiVersion.HEADER_V2_0,ClientType.HEADER_CLIENT_IOS })
//	@Operation(summary = "")

//	ResponseBase findUserCardListV2(AppUserCardEntity entity);
//	
//	@PostMapping(value = "/findUserCardActivationList", headers = { ApiVersion.HEADER_V2_0,ClientType.HEADER_CLIENT_IOS })
//	@Operation(summary = "")

//	ResponseBase findUserCardActivationListV2(AppUserCardEntity entity);

	ResponseBase pcFindUserCardList(AppUserCardEntity entity);

	ResponseBase findUserCardInfo(Integer id);

	ResponseBase findTransaction(CardTransactionEntity entity);

	ResponseBase findCardCvv(PublicEntity entity);

	ResponseBase findCardExpirationTime(PublicEntity entity);

	ResponseBase closeCard(CardCloseEntity entity);

	ResponseBase upTag(UserCardTagEntity entity);

	ResponseBase setPin(CardPinReqEntity entity);

	ResponseBase unfreeze(Integer id);
	
}
