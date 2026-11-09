package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "商户资产响应")
public class MerchantAssetsEntity {

	@Schema(description = "可用金额")
	private Double availableAmount;//可用金额

	@Schema(description = "币种")
	private String currency;//单位

	@Schema(description = "冻结金额")
	private Double frozenAmount;//冻结金额

}
