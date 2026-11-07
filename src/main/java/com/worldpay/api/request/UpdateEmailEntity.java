package com.worldpay.api.request;

import lombok.Data;

@Data
public class UpdateEmailEntity {

	private Integer userBankcardId;
	
	private String email;
	
}
