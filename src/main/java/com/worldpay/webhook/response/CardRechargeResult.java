package com.worldpay.webhook.response;

import lombok.Data;

/**
 * @category webhook回调
 * @author Hlin
 *
 */
@Data
public class CardRechargeResult {
	
	private String eventId;
	
	private String eventType;
	
	//充值结果通知
	private Integer userBankcardId;
	
	private String cardNo;
	
	private String rechargeAmount;
	
//	private String requestOrderId;
	
	private String orderId;
	
	//KYC状态变更通知
	private Integer uid;
	
	private String auditState;
	
	private String auditRemark;
	
	//授权3DS通知
	private String verificationType;
	
	private String otp;
	
	private String authId;
	
	private String transactionAmount;
	
	private String transactionCurrency;
	
	//银行卡状态变更通知
	private String status;
	
	//交易通知
	
	private Long createAt;
	
	private String currency;
	
	private transaction transaction;
	
	//商户充值
	private Double amount;
	
	private String curreny;
	
	private String txTime;
	
	private String txHash;
	
	
	@Data
	public static class transaction {
		
		private String localCurrency;
		
		private String localCurrencyAmt;
		
		private String merchantName;
		
		private Integer transStatus;
		
		private String respCodeDesc;
		
		private Integer transType;
		
		private String balance;
		
		private String feeAmount;
		
		private String feeCurrency;
		
		private String transactionId;
		
		private String originalTransactionId;
		
		
	}
	
	
}
