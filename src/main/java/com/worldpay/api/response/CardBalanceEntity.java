package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "卡片余额响应")
public class CardBalanceEntity {

	@Schema(description = "卡片余额")
	private String balance;
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "退款金额")
	private Double refundAmt;
	
	@Schema(description = "币种")
	private String currency;
	
	@Schema(description = "币种符号")
	private String symbol;
}
