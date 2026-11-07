package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardCloseEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardCloseService {

ResponseBase findUserCardList(CardCloseEntity entity);

}
