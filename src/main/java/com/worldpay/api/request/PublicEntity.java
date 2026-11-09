package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "通用ID请求")
public class PublicEntity {
	
	@Schema(description = "记录主键ID")
	private Integer id;

}
