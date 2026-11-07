package com.worldpay.query;

import lombok.Data;

/**
 * ApplyPay 创建订单参数
 * @author Hlin
 *
 */
@Data
public class ApplyPayRequest {
	
	private String uid;
	
	private String goodsNum;

}
