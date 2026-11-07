package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletTransfetRatesEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletTransfetRatesService {

ResponseBase findList();

ResponseBase add(WalletTransfetRatesEntity entity);

ResponseBase update(WalletTransfetRatesEntity entity);

}
