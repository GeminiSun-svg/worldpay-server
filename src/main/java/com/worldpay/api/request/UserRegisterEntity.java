package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @category 用户注册三方入参
 * @author Hlin
 *
 */
@Data
@Schema(description = "用户注册请求")
public class UserRegisterEntity {
	
	@Schema(description = "用户注册邮箱")
	private String email;
	
	@Schema(description = "注册手机号码")
	private String mobileNumber;
	
	@Schema(description = "手机号国际区号")
	private String mobilePrefix;

}
