package com.worldpay.api.response;

import java.util.List;

import lombok.Data;

@Data
public class MerchantAddressEntity {

	private List<AddressInfo> addressInfo;
	private int confirmBlockCount;
	private int rechargeMinLimit;
	private int subAccountId;
	private String symbol;

	@Data
	public static class AddressInfo {
		private String address;
		private String name;

	}

}
