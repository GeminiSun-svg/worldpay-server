package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.EmailTemplateEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface EmailTemplateService {

ResponseBase add(EmailTemplateEntity entity);

ResponseBase update(EmailTemplateEntity entity);

ResponseBase delete(Integer id);

EmailTemplateEntity findCode(Integer code);

ResponseBase findList(EmailTemplateEntity entity);
//	
//	void send(String userEmail, Integer code,String language, Object... arg);
//	
//	void sendTest(String userEmail, Integer code,String language);

}
