package com.worldpay.api.request;

import lombok.Data;

@Data
public class UnfreezeCardReqEntity {
	
	private Integer userBankcardId;
	
	private boolean enable;

}
