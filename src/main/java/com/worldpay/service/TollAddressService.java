package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.TollAddressEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface TollAddressService {

ResponseBase findAll();

ResponseBase add(TollAddressEntity entity);

ResponseBase update(TollAddressEntity entity);

ResponseBase findList(TollAddressEntity entity);

ResponseBase delete(Integer id);

}
