package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "注销卡请求")
public class CloseCardApiEntity {
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;

}
