package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class CardPinReqEntity {
	
	@Schema(description = "")
private String userEmail;
	
	@Schema(description = "")
private String uid;
	
	@Schema(description = "")
private String pin;
	
	@Schema(description = "")
private Integer userBankcardId;
	
	@Schema(description = "")
private String emailCode;
	
	@Schema(description = "")
private String payPassword;

}
