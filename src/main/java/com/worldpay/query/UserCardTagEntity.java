package com.worldpay.query;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "用户卡片标签参数")
public class UserCardTagEntity {
	
	@Schema(description = "主键ID")
	private Integer id;
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;

	@Schema(description = "标签")
	private String tag;

}
