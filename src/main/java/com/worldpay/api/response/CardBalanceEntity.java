package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class CardBalanceEntity {

	@Schema(description = "")
private String balance;
	
	@Schema(description = "")
private Integer userBankcardId;
	
	@Schema(description = "")
private Double refundAmt;
	
	@Schema(description = "")
private String currency;
	
	@Schema(description = "")
private String symbol;
}
