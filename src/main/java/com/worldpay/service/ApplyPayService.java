package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.query.ApplyPayRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface ApplyPayService {

ResponseBase createPayment(ApplyPayRequest entity);

}
