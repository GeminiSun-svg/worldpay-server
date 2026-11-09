package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category 申请银行卡三方入参
 * @author Hlin
 *
 */
@Data
@Schema(description = "开卡请求")
public class OpenCardEntity {
	
	@Schema(description = "银行卡产品ID")
	private Integer bankcardId;
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "用户居住地址")
	private String residenceAddress;
	
	@Schema(description = "卡片标签名称")
	private String tag;
	
	@Schema(description = "开卡产品ID")
	private Integer productId;
	
	@Schema(description = "收货地址ID")
	private Integer deliveryAddressId;

}
