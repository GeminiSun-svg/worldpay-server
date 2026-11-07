package com.worldpay.service;


import com.worldpay.api.request.RechargeEntity;
import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface MerchantService {

ResponseBase findInfo();

ResponseBase rechargeList(RechargeEntity entity);

}
