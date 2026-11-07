package com.worldpay.query;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class UserCardTagEntity {
	
	@Schema(description = "")
private Integer id;
	
	@Schema(description = "")
private Integer userBankcardId;

	@Schema(description = "")
private String tag;

}
