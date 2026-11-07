package com.worldpay.service;

import com.worldpay.api.request.AppBindChannelEditEntity;
import com.worldpay.api.request.AppBindChannelReqEntity;
import com.worldpay.api.request.ApplicationRecordReqEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.ApplicationRecordEntity;

import io.swagger.v3.oas.annotations.Operation;
/**
 * 类描述：申请记录
 *
 * @author GeminiSun
 * @date 2026/2/4 10:11
 */

public interface ApplicationRecordService {

ResponseBase findApplicationRecord(ApplicationRecordEntity entity);

ResponseBase appFindChannel(AppBindChannelReqEntity appBindChannelReqEntity);

ResponseBase appBindChannel(AppBindChannelEditEntity appBindChannelEditEntity);

ResponseBase appIsChannel(AppBindChannelEditEntity appBindChannelEditEntity);

ResponseBase updateStatus(ApplicationRecordReqEntity entity);
}
