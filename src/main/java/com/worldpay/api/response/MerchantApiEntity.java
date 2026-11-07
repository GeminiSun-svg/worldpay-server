package com.worldpay.api.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MerchantApiEntity {
	
	private BigDecimal availableAmount;
	
	private String currency;
	
	private BigDecimal frozenAmount;
	
}
