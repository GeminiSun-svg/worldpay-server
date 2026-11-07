package com.worldpay.api.request;

import lombok.Data;

/**
 * @category 提现申请对象
 * @author Hlin
 *
 */
@Data
public class WithdrawReqEntity {
	
	private String chain;//网络类型
	
	private String orderNo;//订单号
	
	private String amount;//提现金额
	
	private String coin;//代币类型
	
	private String uid;//用户id
		
	private String address;//钱包地址
	

}
