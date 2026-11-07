package com.worldpay.api.response;

import lombok.Data;

@Data
public class UserAddressReq {
	
	private String addressType;
	
	private String address;
	
	private String countInfo;
	
	private String countUrl;

}
