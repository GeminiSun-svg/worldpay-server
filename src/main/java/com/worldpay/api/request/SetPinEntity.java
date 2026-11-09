package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "设置PIN码请求")
public class SetPinEntity {
	
	@Schema(description = "卡片PIN码")
	private String pin;
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;

}
