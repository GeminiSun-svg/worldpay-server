package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @category 苹果内购解析参数
 * @author Hlin
 *
 */
@Data
@Schema(description = "苹果内购请求")
public class IosPayReqEntity {
	
	@Schema(description = "内购用户ID")
	private String uid;
	
	@Schema(description = "内购商品编号")
	private String goodsNum;
	
	@Schema(description = "苹果支付凭证数据")
	private String receiptData;
	
	@Schema(description = "苹果内购交易号")
	private String receiptNum;

}
