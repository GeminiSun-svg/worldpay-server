package com.worldpay.service;
import org.springframework.web.multipart.MultipartFile;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.card.CardBannerEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardbannerService {

ResponseBase findAll(CardBannerEntity entity);

ResponseBase add(String dataStr,MultipartFile file);

ResponseBase update(String dataStr,MultipartFile file);

ResponseBase delete(Integer id);

ResponseBase findById(String id);

ResponseBase changeStatus(Integer id,Integer statusType);

ResponseBase findNormalInfo();

}
