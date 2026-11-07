package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.firm.FirmAccountEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface FirmAccountService {

ResponseBase add(FirmAccountEntity entity);

ResponseBase update(FirmAccountEntity entity);

ResponseBase delete(Integer id);

ResponseBase find();

ResponseBase findList(FirmAccountEntity entity);

}
