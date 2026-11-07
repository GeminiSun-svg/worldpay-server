package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletToWebRatesEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletToWebRatesService {

ResponseBase find();

ResponseBase update(WalletToWebRatesEntity entity);

}
