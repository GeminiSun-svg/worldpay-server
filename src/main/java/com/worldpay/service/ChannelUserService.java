package com.worldpay.service;

import com.worldpay.api.request.ChannelUserAddEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.channel.ChannelUserEntity;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface ChannelUserService {

ResponseBase findAll(ChannelUserEntity entity,HttpServletRequest request);

ResponseBase appFindAll(ChannelUserEntity entity,HttpServletRequest request);

ResponseBase appFindById(String id);

ResponseBase findChannelUserInfo();
    //增加

ResponseBase add(ChannelUserAddEntity entity, HttpServletRequest request);

    //编辑

ResponseBase update(ChannelUserAddEntity entity);

ResponseBase updatePwd(ChannelUserAddEntity entity);

ResponseBase deleteChannelUser(int userId);

ResponseBase findChannelLevel(HttpServletRequest request);

ResponseBase appFindChannelLevel(HttpServletRequest request);

}
