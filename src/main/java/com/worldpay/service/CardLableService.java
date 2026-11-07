package com.worldpay.service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardLableEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardLableService {

ResponseBase add(CardLableEntity entity);

ResponseBase delete(Integer id);

ResponseBase findList(CardLableEntity entity);

ResponseBase findAll();
	
}
