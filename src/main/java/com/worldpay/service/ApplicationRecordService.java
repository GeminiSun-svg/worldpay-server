package com.worldpay.service;

import com.worldpay.api.request.AppBindChannelEditEntity;
import com.worldpay.api.request.AppBindChannelReqEntity;
import com.worldpay.api.request.ApplicationRecordReqEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.ApplicationRecordEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
/**
 * 类描述：申请记录
 *
 * @author GeminiSun
 * @date 2026/2/4 10:11
 */

public interface ApplicationRecordService {

ResponseBase findApplicationRecord(ApplicationRecordEntity entity);

ResponseBase appFindChannel(@RequestBody AppBindChannelReqEntity appBindChannelReqEntity);

ResponseBase appBindChannel(@RequestBody AppBindChannelEditEntity appBindChannelEditEntity);

ResponseBase appIsChannel(@RequestBody AppBindChannelEditEntity appBindChannelEditEntity);

ResponseBase updateStatus(@RequestBody ApplicationRecordReqEntity entity);
}
