package com.worldpay.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ApplyPay 创建订单参数
 * @author Hlin
 *
 */
@Data
@Schema(description = "ApplyPay创建订单参数")
public class ApplyPayRequest {
	
	@Schema(description = "用户ID")
	private String uid;
	
	@Schema(description = "商品编号")
	private String goodsNum;

}
