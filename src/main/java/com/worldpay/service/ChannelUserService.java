package com.worldpay.service;

import com.worldpay.api.request.ChannelUserAddEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.channel.ChannelUserEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface ChannelUserService {

ResponseBase findAll(@RequestBody ChannelUserEntity entity,HttpServletRequest request);

ResponseBase appFindAll(ChannelUserEntity entity,HttpServletRequest request);

ResponseBase appFindById(@PathVariable String id);

ResponseBase findChannelUserInfo();
    //增加

ResponseBase add(@RequestBody ChannelUserAddEntity entity, HttpServletRequest request);

    //编辑

ResponseBase update(@RequestBody ChannelUserAddEntity entity);

ResponseBase updatePwd(@RequestBody ChannelUserAddEntity entity);

ResponseBase deleteChannelUser(int userId);

ResponseBase findChannelLevel(HttpServletRequest request);

ResponseBase appFindChannelLevel(HttpServletRequest request);

}
