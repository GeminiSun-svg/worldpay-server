package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "卡片数据响应")
public class CardDataResEntity {

	@Schema(description = "卡片ID")
	private Integer bankCardId;

	@Schema(description = "卡片来源")
	private String bankCardSource;

	@Schema(description = "卡模板ID")
	private Integer templateId;

	@Schema(description = "卡号")
	private String cardNo;

	@Schema(description = "用户银行卡状态")
	private String userBankCardStatus;

}
