package com.worldpay.api.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "重置钱包地址请求")
public class ResetAddressEntity {
	
	@NotBlank(message = "原始地址不能为空")
	@Schema(description = "原始钱包地址")
	private String originalAddress;
	
	@NotBlank(message = "新地址不能为空")
	@Schema(description = "新的钱包地址")
	private String newAddress;
	
	@NotBlank(message = "签名不能为空")
    @Schema(description = "接口请求签名")
	private String sign;

}
