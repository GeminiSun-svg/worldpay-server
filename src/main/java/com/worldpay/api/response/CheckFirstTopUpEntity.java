package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "首次充值检查响应")
public class CheckFirstTopUpEntity {

	@Schema(description = "是否首次充值")
	private Boolean isFirst;

	@Schema(description = "开卡金额")
	private Double OpenCardAmount;
}
