package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.channel.ChannelCardEntity;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface ChannelCardService {

ResponseBase findAll(ChannelCardEntity entity);

ResponseBase appFindAll(ChannelCardEntity entity, HttpServletRequest request);

ResponseBase findById(Integer id);

    //编辑

ResponseBase update(ChannelCardEntity entity);

ResponseBase oneClickUpdate();

}
