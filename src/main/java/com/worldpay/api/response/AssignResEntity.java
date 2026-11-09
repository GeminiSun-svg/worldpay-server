package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "卡片分配响应")
public class AssignResEntity {

	@Schema(description = "用户银行卡ID")
	private String userBankcardId;

	@Schema(description = "卡号")
	private String cardNo;

}
