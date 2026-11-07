package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysBrokerageRatesEntity;


import io.swagger.v3.oas.annotations.Operation;

public interface SysBrokerageRatesService {

ResponseBase findAll(SysBrokerageRatesEntity entity);

    //增加

ResponseBase add(SysBrokerageRatesEntity entity);

    //编辑

ResponseBase update(SysBrokerageRatesEntity entity);
}
