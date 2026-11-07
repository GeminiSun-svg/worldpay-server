package com.worldpay.service;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.NoticeEntity;

import io.swagger.v3.oas.annotations.Operation;
/**
 *  @category 通知接口
 */

public interface NoticeService {

ResponseBase findAll(NoticeEntity entity);

ResponseBase findById(Integer id);

ResponseBase findList(NoticeEntity entity);

    //增加

ResponseBase add(NoticeEntity entity);

    //编辑

ResponseBase update(NoticeEntity entity);

    //删除

ResponseBase delete(Integer id);

ResponseBase send(Integer id);
}
