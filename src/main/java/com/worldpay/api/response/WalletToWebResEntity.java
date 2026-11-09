package com.worldpay.api.response;

import java.math.BigDecimal;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "钱包提现响应")
public class WalletToWebResEntity {

	@Schema(description = "申请金额")
	private BigDecimal originalAmount;//申请金额

	@Schema(description = "手续费")
	private BigDecimal fee;//手续费

	@Schema(description = "服务费")
	private BigDecimal serverAmount;//服务费

	@Schema(description = "实际到账金额")
	private BigDecimal realAmount;//实际到账金额

	@Schema(description = "费率")
	private BigDecimal handlingRates;//费率

	@Schema(description = "最小充值金额")
	private Double minAmount;//最小充值金额

	@Schema(description = "最大充值金额")
	private Double maxAmount;//最大充值金额

}
