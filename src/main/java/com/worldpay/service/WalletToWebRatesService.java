package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletToWebRatesEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletToWebRatesService {

ResponseBase find();

ResponseBase update(WalletToWebRatesEntity entity);

}
