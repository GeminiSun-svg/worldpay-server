package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "卡片信息请求")
public class CardInfoEntity {
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "卡片CVV安全码")
	private String cvv;
	
	@Schema(description = "查询信息类型")
	private Integer infoType;
	
	@Schema(description = "卡片有效期限")
	private String expireDate;
	
	@Schema(description = "完整银行卡号")
	private String cardNumber;
	
	@Schema(description = "银行卡卡片状态")
	private Integer status;

}
