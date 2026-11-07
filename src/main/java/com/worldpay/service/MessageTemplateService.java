package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.MessageTemplateEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface MessageTemplateService {

ResponseBase add(MessageTemplateEntity entity);

ResponseBase update(MessageTemplateEntity entity);

ResponseBase delete(Integer id);

MessageTemplateEntity findCode(Integer code);

ResponseBase findList(MessageTemplateEntity entity);

}
