package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface KycService {

	ResponseBase apply(Integer applyId);

}
