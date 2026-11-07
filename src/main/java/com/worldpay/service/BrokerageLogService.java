package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.brokerage.BrokerageLogEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface BrokerageLogService {

ResponseBase findAll(@RequestBody BrokerageLogEntity entity, HttpServletRequest request);

ResponseBase findByChannelId(Integer ChannelId);

ResponseBase findBrokerageLogById(@PathVariable Integer id);

}
