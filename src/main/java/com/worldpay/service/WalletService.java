package com.worldpay.service;
import com.worldpay.api.request.WithdrawalCallbackEntity;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.api.response.UsdtTopinNotifyEntity;
import com.worldpay.query.PublicQueryEntity;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.entity.wallet.WalletManualLogEntity;
import com.worldpay.entity.wallet.WalletTransfetListEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletService {

ResponseBase topinUsdtAddress(String uid);

ApiResponseEntity topinUsdtNotify(UsdtTopinNotifyEntity entity,HttpServletRequest req);

ResponseBase walletLog(WalletLogEntity entity);

ResponseBase walletLogStat(PublicQueryEntity entity);

ResponseBase walletTopUp(WalletManualLogEntity entity,HttpServletRequest req);

ResponseBase findWalletLog(WalletLogEntity entity);

ResponseBase transfer(WalletTransfetListEntity entity);

ResponseBase transferReading(Double sendMoney,String uid);

ResponseBase findReading();

ApiResponseEntity withdrawalNotify(WithdrawalCallbackEntity entity);

}
