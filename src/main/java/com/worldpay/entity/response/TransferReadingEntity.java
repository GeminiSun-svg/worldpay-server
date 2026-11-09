package com.worldpay.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "转账费率信息")
public class TransferReadingEntity {
	
	@Schema(description = "转账费率")
	private Double sendRates;
	
	@Schema(description = "手续费")
	private Double handlingFee;
	
	@Schema(description = "实际到账金额")
	private Double actualMoney;

}
