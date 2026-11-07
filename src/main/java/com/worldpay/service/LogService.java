package com.worldpay.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysLogEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface LogService {

ResponseBase findList(SysLogEntity entity);

}
