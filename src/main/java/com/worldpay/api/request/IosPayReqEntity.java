package com.worldpay.api.request;

import lombok.Data;

/**
 * @category 苹果内购解析参数
 * @author Hlin
 *
 */
@Data
public class IosPayReqEntity {
	
	private String uid;
	
	private String goodsNum;
	
	private String receiptData;
	
	private String receiptNum;

}
