package com.worldpay.service;

import java.math.BigDecimal;

import jakarta.servlet.http.HttpServletRequest;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletToWebLogEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletToWebLogService {

ResponseBase create(WalletToWebLogEntity entity);

ResponseBase pass(Integer id,Integer gooleCode,HttpServletRequest request);

ResponseBase reject(Integer id,String rejectContent);

ResponseBase findRates();

ResponseBase findList(WalletToWebLogEntity entity);

ResponseBase calculateAmount(BigDecimal amount);
	

}
