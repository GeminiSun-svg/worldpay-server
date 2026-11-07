package com.worldpay.service;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardLableEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardLableService {

ResponseBase add(CardLableEntity entity);

ResponseBase delete(Integer id);

ResponseBase findList(CardLableEntity entity);

ResponseBase findAll();
	
}
