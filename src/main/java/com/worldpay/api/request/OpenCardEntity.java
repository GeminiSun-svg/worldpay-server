package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category 申请银行卡三方入参
 * @author Hlin
 *
 */
@Data
public class OpenCardEntity {
	
	@Schema(description = "")
private Integer bankcardId;
	
	@Schema(description = "")
private Integer userBankcardId;
	
	@Schema(description = "")
private String residenceAddress;
	
	@Schema(description = "")
private String tag;
	
	@Schema(description = "")
private Integer productId;
	
	@Schema(description = "")
private Integer deliveryAddressId;

}
