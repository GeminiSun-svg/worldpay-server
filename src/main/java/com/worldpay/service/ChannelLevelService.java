package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.brokerage.ChannelLevelEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;

public interface ChannelLevelService {

ResponseBase findAll(@RequestBody ChannelLevelEntity entity);

    //增加

ResponseBase add(@RequestBody ChannelLevelEntity entity);

    //编辑

ResponseBase update(@RequestBody ChannelLevelEntity entity);

ResponseBase updateStatus(int id, int applyState);

ResponseBase delete(int id);

}
