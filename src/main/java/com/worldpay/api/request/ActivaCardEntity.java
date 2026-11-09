package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "激活卡请求")
public class ActivaCardEntity {

	@Schema(description = "开卡产品ID")
	private Integer productId;
	
	@Schema(description = "待激活银行卡号")
	private String cardNo;
	
	@Schema(description = "手机短信验证码")
	private String verifyCode;
	
	@Schema(description = "手机号国际区号")
	private String mobilePrefix;
	
	@Schema(description = "用户手机号码")
	private String mobile;
	
	@Schema(description = "国家地区代码")
	private String countryCode;
	
	@Schema(description = "用户联系地址")
	private String address;
	
	@Schema(description = "备用联系地址")
	private String address2;
	
	@Schema(description = "地址所在城市")
	private String city;
	
	@Schema(description = "所在省份或州")
	private String state;
	
	@Schema(description = "地址邮政编码")
	private String postCode;
	
	
	
}
