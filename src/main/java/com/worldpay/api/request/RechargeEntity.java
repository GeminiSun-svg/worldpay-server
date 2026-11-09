package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "充值记录查询请求")
public class RechargeEntity {
	
	@Schema(description = "分页当前页码")
	private Integer pageNum;
	
	@Schema(description = "分页每页条数")
	private Integer pageSize;

}
