package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category 银行卡充值三方入参
 * @author Hlin
 *
 */
@Data
@Schema(description = "卡片充值请求")
public class RechargeCardEntity {
	
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@Schema(description = "充值支付总金额")
	private Double amount;
	
	@Schema(description = "实际到账金额")
	private Double targetAmount;
	
	@Schema(description = "充值手续费用")
	private Double handlingFees;
	
	@Schema(description = "充值用户ID")
	private String uid;
	
	@Schema(description = "充值支付方式")
	private Integer payType;
	
	@Schema(description = "后台操作人ID")
	private Integer setUser;
	
	@Schema(description = "后台操作人姓名")
	private String setUserName;
	
	@Schema(description = "用户支付密码")
	private String payPasswrod;
	
	@Schema(description = "充值类型标识")
	private Boolean type;
	
	@Schema(description = "三方请求订单号")
	private String requestOrderId;
	
	@Schema(description = "开卡费用金额")
	private Double openCardAmount;
	
	@Schema(description = "是否首次充值")
	private Boolean isFirst;

}
