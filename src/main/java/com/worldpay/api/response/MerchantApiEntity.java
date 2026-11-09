package com.worldpay.api.response;

import java.math.BigDecimal;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "商户API资产响应")
public class MerchantApiEntity {

	@Schema(description = "可用金额")
	private BigDecimal availableAmount;

	@Schema(description = "币种")
	private String currency;

	@Schema(description = "冻结金额")
	private BigDecimal frozenAmount;
	
}
