package com.worldpay.entity.response;

import java.math.BigDecimal;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.worldpay.utils.BigDecimalThreeDecimalSerializer;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "统计结果")
public class AdminStatEntity {
	
	@Schema(description = "商户可用金额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal merchantAvailableAmount;
	
	@Schema(description = "商户冻结金额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal merchantFrozenAmount;
	
	@Schema(description = "注册用户数")
	private Integer registerUserNum;
	
	@Schema(description = "充值用户数")
	private Integer topupUserNum;
	
	@Schema(description = "开卡用户数")
	private Integer openCardUserNum;
	
	@Schema(description = "钱包总额")
	private Double walletTotal;
	
	@Schema(description = "钱包冻结总额")
	private Double walletFreezeTotal;
	
	@Schema(description = "卡片总额")
	private Double cardTotal;
	
	@Schema(description = "卡片充值总额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardTopupTotal;
	
	@Schema(description = "卡片充值手续费")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardTopupFee;
	
	@Schema(description = "卡片消费总额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardConsumeTotal;
	
	@Schema(description = "卡片消费手续费")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardConsumeFee;
	
	@Schema(description = "开卡费用总额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal openCardFee;

	@Schema(description = "虚拟卡开卡数")
	private Integer openCardViNum;
	
	@Schema(description = "实体卡开卡数")
	private Integer openCardPhNum;

	@Schema(description = "佣金总额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    public BigDecimal borkerageFee;

	@Schema(description = "历史佣金总额")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    public BigDecimal oldBorkerageFee;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "渠道名称")
	private String channelName;

}
