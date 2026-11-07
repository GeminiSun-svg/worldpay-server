package com.worldpay.service;


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
