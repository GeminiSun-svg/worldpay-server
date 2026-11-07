package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.channel.ChannelCardEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface ChannelCardService {

ResponseBase findAll(@RequestBody ChannelCardEntity entity);

ResponseBase appFindAll(ChannelCardEntity entity, HttpServletRequest request);

ResponseBase findById(@PathVariable Integer id);

    //编辑

ResponseBase update(@RequestBody ChannelCardEntity entity);

ResponseBase oneClickUpdate();

}
