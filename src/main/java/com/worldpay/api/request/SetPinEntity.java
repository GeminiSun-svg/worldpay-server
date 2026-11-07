package com.worldpay.api.request;

import lombok.Data;

@Data
public class SetPinEntity {
	
	private String pin;
	
	private Integer userBankcardId;

}
