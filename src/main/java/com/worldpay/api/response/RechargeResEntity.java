package com.worldpay.api.response;

import java.math.BigDecimal;


import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "充值响应")
public class RechargeResEntity {
	
	@Schema(description = "主键ID")
	private Integer id;
	
	@Schema(description = "操作金额")
	private BigDecimal operateAmount;
	
	@Schema(description = "可用金额")
	private BigDecimal availableAmount;
	
	@Schema(description = "冻结金额")
	private BigDecimal frozenAmount;
	
	@Schema(description = "创建时间戳")
	private Long createAt;
	
	@Schema(description = "币种")
	private String currency;

}
