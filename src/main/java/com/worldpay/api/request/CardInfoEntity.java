package com.worldpay.api.request;

import lombok.Data;

@Data
public class CardInfoEntity {
	
	private Integer userBankcardId;
	
	private String cvv;
	
	private Integer infoType;
	
	private String expireDate;
	
	private String cardNumber;
	
	private Integer status;

}
