package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.PayTypeSateEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface PayTypeSateService {

ResponseBase findAll();

ResponseBase findManage();

ResponseBase update(PayTypeSateEntity entity);

ResponseBase findList(PayTypeSateEntity entity);

ResponseBase add(PayTypeSateEntity entity);
	

}
