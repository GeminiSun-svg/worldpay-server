package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "卡片PIN码请求")
public class CardPinReqEntity {
	
	@Schema(description = "用户邮箱地址")
	private String userEmail;
	
	@Schema(description = "操作用户ID")
	private String uid;
	
	@Schema(description = "卡片PIN码")
	private String pin;
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "邮箱验证码")
	private String emailCode;
	
	@Schema(description = "用户支付密码")
	private String payPassword;

}
