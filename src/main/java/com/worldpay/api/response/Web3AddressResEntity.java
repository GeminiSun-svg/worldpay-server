package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "Web3地址响应")
public class Web3AddressResEntity {

	@Schema(description = "地址信息")
	private Address address;

	@Schema(description = "BNB链地址")
	private String bnbAddress;

	@Schema(description = "BTC链地址")
	private String btcAddress;

	@Schema(description = "EVM链地址")
	private String evmAddress;

	@Schema(description = "ETH链地址")
	private String ethAddress;

	@Schema(description = "TRON链地址")
	private String tronAddress;

	@Schema(description = "用户ID")
	private String uid;

	@Data
	public static class Address {
		@Schema(description = "BNB地址")
		private String bnb;

		@Schema(description = "BTC地址")
		private String btc;

		@Schema(description = "ETH地址")
		private String eth;

		@Schema(description = "TRON地址")
		private String tron;
	}

}
