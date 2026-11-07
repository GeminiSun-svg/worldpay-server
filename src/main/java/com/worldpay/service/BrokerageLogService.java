package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.brokerage.BrokerageLogEntity;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface BrokerageLogService {

ResponseBase findAll(BrokerageLogEntity entity, HttpServletRequest request);

ResponseBase findByChannelId(Integer ChannelId);

ResponseBase findBrokerageLogById(Integer id);

}
