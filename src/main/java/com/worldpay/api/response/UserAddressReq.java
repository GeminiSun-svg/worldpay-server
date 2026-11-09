package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "用户地址响应")
public class UserAddressReq {

	@Schema(description = "地址类型")
	private String addressType;

	@Schema(description = "地址")
	private String address;

	@Schema(description = "配置内容")
	private String countInfo;

	@Schema(description = "配置链接")
	private String countUrl;

}
