package com.worldpay.entity.response;

import lombok.Data;

@Data
public class TransferReadingEntity {
	
	private Double sendRates;
	
	private Double handlingFee;
	
	private Double actualMoney;

}
