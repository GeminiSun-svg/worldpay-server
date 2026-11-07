package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category 银行卡充值三方入参
 * @author Hlin
 *
 */
@Data
public class RechargeCardEntity {
	
	@Schema(description = "")
private Integer userBankcardId;
	
	@Schema(description = "")
private Double amount;
	
	@Schema(description = "")
private Double targetAmount;
	
	@Schema(description = "")
private Double handlingFees;
	
	@Schema(description = "")
private String uid;
	
	@Schema(description = "")
private Integer payType;
	
	@Schema(description = "")
private Integer setUser;
	
	@Schema(description = "")
private String setUserName;
	
	@Schema(description = "")
private String payPasswrod;
	
	@Schema(description = "")
private Boolean type;
	
	@Schema(description = "")
private String requestOrderId;
	
	@Schema(description = "")
private Double openCardAmount;
	
	@Schema(description = "")
private Boolean isFirst;

}
