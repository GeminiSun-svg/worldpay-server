package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "冻结解冻卡请求")
public class UnfreezeCardReqEntity {
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "是否启用卡片")
	private boolean enable;

}
