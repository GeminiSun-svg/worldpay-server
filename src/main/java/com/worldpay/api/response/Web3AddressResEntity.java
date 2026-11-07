package com.worldpay.api.response;

import lombok.Data;

@Data
public class Web3AddressResEntity {

	private Address address;
	
	private String bnbAddress;
	
	private String btcAddress;
	
	private String evmAddress;
	
	private String ethAddress;
	
	private String tronAddress;
	
	private String uid;

	@Data
	public static class Address {
		private String bnb;
		
		private String btc;
		
		private String eth;
		
		private String tron;
	}

}
