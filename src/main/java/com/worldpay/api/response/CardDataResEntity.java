package com.worldpay.api.response;

import lombok.Data;

@Data
public class CardDataResEntity {
	
	private Integer bankCardId;
	
	private String bankCardSource;
	
	private Integer templateId;
	
	private String cardNo;
	
	private String userBankCardStatus;

}
