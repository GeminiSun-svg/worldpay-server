package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.IpWhitelistEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface IpWhitelistService {

ResponseBase findUserCardAssets(IpWhitelistEntity entity);

ResponseBase add(IpWhitelistEntity entity);

ResponseBase delete(Integer id);

}
