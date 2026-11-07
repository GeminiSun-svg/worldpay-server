package com.worldpay.query;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "")
public class ProductPayEntity {
	
	@NotBlank(message = "用户uid不能为空")
	@Schema(description = "")
private String uid;
	
	@NotBlank(message = "产品id不能为空")
	@Schema(description = "")
private Integer productId;
	
	@NotBlank(message = "购买金额不能为空")
	@Schema(description = "")
private Double payMonery;
	
	@NotBlank(message = "购买份数不能为空")
	@Schema(description = "")
private Integer payNum;
	
	@NotBlank(message = "支付密码不能为空")
	@Schema(description = "")
private String payPassword;

}
