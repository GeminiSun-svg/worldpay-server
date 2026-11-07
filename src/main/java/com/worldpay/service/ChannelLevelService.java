package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.brokerage.ChannelLevelEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface ChannelLevelService {

ResponseBase findAll(ChannelLevelEntity entity);

    //增加

ResponseBase add(ChannelLevelEntity entity);

    //编辑

ResponseBase update(ChannelLevelEntity entity);

ResponseBase updateStatus(int id, int applyState);

ResponseBase delete(int id);

}
