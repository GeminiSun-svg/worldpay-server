package com.worldpay.query;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "理财产品购买参数")
public class ProductPayEntity {
	
	@NotBlank(message = "用户uid不能为空")
	@Schema(description = "用户ID")
	private String uid;
	
	@NotBlank(message = "产品id不能为空")
	@Schema(description = "产品ID")
	private Integer productId;
	
	@NotBlank(message = "购买金额不能为空")
	@Schema(description = "购买金额")
	private Double payMonery;
	
	@NotBlank(message = "购买份数不能为空")
	@Schema(description = "购买份数")
	private Integer payNum;
	
	@NotBlank(message = "支付密码不能为空")
	@Schema(description = "支付密码")
	private String payPassword;

}
