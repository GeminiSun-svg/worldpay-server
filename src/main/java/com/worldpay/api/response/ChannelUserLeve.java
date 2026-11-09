package com.worldpay.api.response;

import java.math.BigDecimal;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "渠道商等级响应")
public class ChannelUserLeve {


	@Schema(description = "当前渠道商ID")
	private Integer currentUserId;//当前渠道商id
	
	@Schema(description = "当前渠道商用户ID")
	private Integer currentUserUId;//当前渠道商uid
	
	@Schema(description = "下级渠道商ID")
	private Integer lowerUserId;//下级渠道商id
	
	@Schema(description = "当前渠道商等级")
	private Integer currentLevel;//当前渠道商等级()
	
	@Schema(description = "当前渠道商等级名称")
	private String currentLevelName;//当前渠道商等级名称
	
	@Schema(description = "层级ID（1、2、3）")
	private Integer leveId;//层级属于1、2、3
	
	@Schema(description = "佣金比例（直推配置）")
	private BigDecimal ratesFee;//佣金比例（当前渠道商直推配置比例）
	
	@Schema(description = "佣金比例（实际计算）")
	private BigDecimal actualRatesFee;//佣金比例（实际计算拥挤的比例，上级佣金比例-下级佣金比例）
	
	
	
}
