package com.worldpay.service;


import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface WalletEntranceService {

ResponseBase findList();

ResponseBase operate(Boolean type,String userName);

}
