package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.merchant.RechargeHistoryEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface RechargeHistoryService {

ResponseBase findList(RechargeHistoryEntity entity);

}
