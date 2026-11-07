package com.worldpay.api.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChannelUserLeve {


	private Integer currentUserId;//当前渠道商id
	
	private Integer currentUserUId;//当前渠道商uid
	
	private Integer lowerUserId;//下级渠道商id
	
	private Integer currentLevel;//当前渠道商等级()
	
	private String currentLevelName;//当前渠道商等级名称
	
	private Integer leveId;//层级属于1、2、3
	
	private BigDecimal ratesFee;//佣金比例（当前渠道商直推配置比例）
	
	private BigDecimal actualRatesFee;//佣金比例（实际计算拥挤的比例，上级佣金比例-下级佣金比例）
	
	
	
}
