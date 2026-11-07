package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
//	@GetMapping("/send")
//	@Operation(summary = "")
//	void send(String userEmail, Integer code,String language, Object... arg);
//	
//	@GetMapping("/sendTest")
//	@Operation(summary = "")
//	void sendTest(String userEmail, Integer code,String language);

}
