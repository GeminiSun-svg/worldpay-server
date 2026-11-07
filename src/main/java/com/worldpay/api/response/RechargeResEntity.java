package com.worldpay.api.response;

import java.math.BigDecimal;


import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class RechargeResEntity {
	
	@Schema(description = "")
private Integer id;
	
	@Schema(description = "")
private BigDecimal operateAmount;
	
	@Schema(description = "")
private BigDecimal availableAmount;
	
	@Schema(description = "")
private BigDecimal frozenAmount;
	
	@Schema(description = "")
private Long createAt;
	
	@Schema(description = "")
private String currency;

}
