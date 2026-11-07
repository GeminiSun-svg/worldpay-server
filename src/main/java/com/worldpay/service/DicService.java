package com.worldpay.service;


import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface DicService {

ResponseBase getLanguage();

ResponseBase getCountryCode();

ResponseBase findCountry(Integer parentId);

ResponseBase findCountry();

ResponseBase findPayType();

ResponseBase findKycState();

ResponseBase findUserState();

ResponseBase findProtocolType();

ResponseBase findJumpAddress();

ResponseBase findSysConfigType();

ResponseBase findNetwokList();

ResponseBase getNetwokList();

ResponseBase findRecordState();

ResponseBase getWalletOperationType();
	
}

