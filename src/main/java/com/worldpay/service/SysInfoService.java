package com.worldpay.service;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysInfoEntity;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
/**
 *  @category 配置接口
 */

public interface SysInfoService {

ResponseBase findAll(SysInfoEntity entity);

ResponseBase findById(Integer id);
	
    //增加

ResponseBase add(SysInfoEntity entity);

    //编辑

ResponseBase update(SysInfoEntity entity);

    //删除

ResponseBase delete(Integer id);

ResponseBase changeStatus(Integer id,Integer statusType);

ResponseBase findConfigInfo(Integer configType);
	
}
