package com.worldpay.api.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ResetAddressEntity {
	
	@NotBlank(message = "原始地址不能为空")
	@Schema(description = "")
private String originalAddress;
	
	@NotBlank(message = "新地址不能为空")
	@Schema(description = "")
private String newAddress;
	
	@NotBlank(message = "签名不能为空")
    @Schema(description = "")
private String sign;

}
