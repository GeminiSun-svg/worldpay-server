package com.worldpay.api.response;

import lombok.Data;

@Data
public class MerchantAssetsEntity {
	
	private Double availableAmount;//可用金额
	
	private String currency;//单位
	
	private Double frozenAmount;//冻结金额

}
