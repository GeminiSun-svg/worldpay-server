package com.worldpay.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "")
public class UpdatePwdEntity {

	@NotBlank(message = "用户uid不能为空")
	@Schema(description = "")
private String uid;
	
	@NotBlank(message = "原密码不能为空")
	@Schema(description = "")
private String formerPassword;
	
	@NotBlank(message = "新密码不能为空")
	@Schema(description = "")
private String newPassword;
	
	@Schema(description = "")
private String emailCode;

}
