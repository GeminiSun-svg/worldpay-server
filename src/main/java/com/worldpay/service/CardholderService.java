package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserHolderEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardholderService {

ResponseBase add(AppUserHolderEntity entity);

ResponseBase update(AppUserHolderEntity entity);

ResponseBase delete(Integer id);

ResponseBase findByUid(String uid);

ResponseBase findById(Integer id);

}
