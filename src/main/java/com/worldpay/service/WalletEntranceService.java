package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletEntranceService {

ResponseBase findList();

ResponseBase operate(Boolean type,String userName);

}
