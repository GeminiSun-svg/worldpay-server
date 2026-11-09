package com.worldpay.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "修改密码参数")
public class UpdatePwdEntity {

	@NotBlank(message = "用户uid不能为空")
	@Schema(description = "用户ID")
	private String uid;
	
	@NotBlank(message = "原密码不能为空")
	@Schema(description = "原密码")
	private String formerPassword;
	
	@NotBlank(message = "新密码不能为空")
	@Schema(description = "新密码")
	private String newPassword;
	
	@Schema(description = "邮箱验证码")
	private String emailCode;

}
