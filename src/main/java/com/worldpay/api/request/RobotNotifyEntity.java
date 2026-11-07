package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class RobotNotifyEntity {
	
	@Schema(description = "")
private Integer type;
	
	@Schema(description = "")
private String content;
	
	@Schema(description = "")
private String sign;

}
