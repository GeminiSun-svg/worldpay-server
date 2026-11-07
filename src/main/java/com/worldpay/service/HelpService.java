package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.HelpEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface HelpService {

ResponseBase add(HelpEntity entity);

ResponseBase update(HelpEntity entity);

ResponseBase delete(Integer id);

ResponseBase findList(HelpEntity entity);

ResponseBase pcFindList(HelpEntity entity);

ResponseBase findById(Integer id);

}
