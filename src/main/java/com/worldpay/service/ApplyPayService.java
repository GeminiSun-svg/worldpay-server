package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.query.ApplyPayRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface ApplyPayService {

ResponseBase createPayment(ApplyPayRequest entity);

}
