package com.worldpay.service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardSynopsisEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardSynopsisService {

ResponseBase add(CardSynopsisEntity entity);

ResponseBase update(CardSynopsisEntity entity);

ResponseBase delete(Integer id);

ResponseBase findList(CardSynopsisEntity entity);

ResponseBase findAll();

}
