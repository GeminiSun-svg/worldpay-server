package com.worldpay.api.request;
import lombok.Data;

/**
 * @category 提现回调
 * @author Hlin
 *
 */
@Data
public class WithdrawalCallbackEntity {
	
	private String txHash;

	private String outOrderNo;
	
	private String orderNo;
	
	private String status;
	
    private String address;
    
    private String chain;
    
    private String sign;
}
