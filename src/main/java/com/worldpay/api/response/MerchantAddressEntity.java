package com.worldpay.api.response;

import java.util.List;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "商户充值地址响应")
public class MerchantAddressEntity {

	@Schema(description = "地址信息列表")
	private List<AddressInfo> addressInfo;
	@Schema(description = "确认区块数")
	private int confirmBlockCount;
	@Schema(description = "最小充值限额")
	private int rechargeMinLimit;
	@Schema(description = "子账户ID")
	private int subAccountId;
	@Schema(description = "币种符号")
	private String symbol;

	@Data
	public static class AddressInfo {
		@Schema(description = "充值地址")
		private String address;
		@Schema(description = "地址名称")
		private String name;

	}

}
