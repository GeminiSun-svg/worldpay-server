package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "修改邮箱请求")
public class UpdateEmailEntity {

	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "用户邮箱地址")
	private String email;
	
}
