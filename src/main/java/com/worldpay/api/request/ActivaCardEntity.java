package com.worldpay.api.request;

import lombok.Data;

@Data
public class ActivaCardEntity {

	private Integer productId;
	
	private String cardNo;
	
	private String verifyCode;
	
	private String mobilePrefix;
	
	private String mobile;
	
	private String countryCode;
	
	private String address;
	
	private String address2;
	
	private String city;
	
	private String state;
	
	private String postCode;
	
	
	
}
