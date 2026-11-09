package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "机器人通知请求")
public class RobotNotifyEntity {
	
	@Schema(description = "通知机器人类型")
	private Integer type;
	
	@Schema(description = "机器人通知内容")
	private String content;
	
	@Schema(description = "接口请求签名")
	private String sign;

}
