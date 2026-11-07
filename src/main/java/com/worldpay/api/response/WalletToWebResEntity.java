package com.worldpay.api.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WalletToWebResEntity {
	
	private BigDecimal originalAmount;//申请金额
	
	private BigDecimal fee;//手续费
	
	private BigDecimal serverAmount;//服务费
	
	private BigDecimal realAmount;//实际到账金额
	
	private BigDecimal handlingRates;//费率
	
	private Double minAmount;//最小充值金额
	
	private Double maxAmount;//最大充值金额

}
