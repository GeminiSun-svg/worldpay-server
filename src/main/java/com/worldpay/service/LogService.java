package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysLogEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface LogService {

ResponseBase findList(SysLogEntity entity);

}
