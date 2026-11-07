package com.worldpay.entity.response;

import java.math.BigDecimal;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.worldpay.utils.BigDecimalThreeDecimalSerializer;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class AdminStatEntity {
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal merchantAvailableAmount;
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal merchantFrozenAmount;
	
	@Schema(description = "")
private Integer registerUserNum;
	
	@Schema(description = "")
private Integer topupUserNum;
	
	@Schema(description = "")
private Integer openCardUserNum;
	
	@Schema(description = "")
private Double walletTotal;
	
	@Schema(description = "")
private Double walletFreezeTotal;
	
	@Schema(description = "")
private Double cardTotal;
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardTopupTotal;
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardTopupFee;
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardConsumeTotal;
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal cardConsumeFee;
	
	@Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    private BigDecimal openCardFee;

	@Schema(description = "")
private Integer openCardViNum;
	
	@Schema(description = "")
private Integer openCardPhNum;

    @Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    public BigDecimal borkerageFee;

    @Schema(description = "")
@JsonSerialize(using = BigDecimalThreeDecimalSerializer.class)
    public BigDecimal oldBorkerageFee;

    @Schema(description = "")
private String userId;

    @Schema(description = "")
private String channelName;

}
