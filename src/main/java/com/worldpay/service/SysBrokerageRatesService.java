package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysBrokerageRatesEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;

public interface SysBrokerageRatesService {

ResponseBase findAll(SysBrokerageRatesEntity entity);

    //增加

ResponseBase add(@RequestBody SysBrokerageRatesEntity entity);

    //编辑

ResponseBase update(@RequestBody SysBrokerageRatesEntity entity);
}
