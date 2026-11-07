package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.wallet.WalletNetworkTopUpEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletNetworkTopUpService {

ResponseBase add(WalletNetworkTopUpEntity entity);

ResponseBase findList(WalletNetworkTopUpEntity entity);

ResponseBase close(Integer id,String reviewRemark);

}
