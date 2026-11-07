package com.worldpay.api.response;

import lombok.Data;

@Data
public class CheckFirstTopUpEntity {

	private Boolean isFirst;
	
	private Double OpenCardAmount;
}
