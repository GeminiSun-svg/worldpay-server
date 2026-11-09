package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * USDT充值回调
 */
@Data
@Schema(description = "USDT充值回调响应")
public class UsdtTopinNotifyEntity {
	
    @Schema(description = "通知类型")
	private String type;
    
    @Schema(description = "区块高度")
	private String block;
   
    @Schema(description = "用户ID")
	private String uid;
    
    @Schema(description = "用户邮箱")
	private String email;

    @Schema(description = "订单号")
	private String orderNo;
    
    @Schema(description = "链名称")
	private String chain;

    @Schema(description = "币种")
	private String coin;

    @Schema(description = "金额")
	private String amount;
    
    @Schema(description = "原始金额")
	private String amount_raw;
    
    @Schema(description = "交易时间")
	private String time;

    @Schema(description = "转出地址")
	private String outaddress;

    @Schema(description = "转入地址")
	private String inaddress;

    @Schema(description = "签名")
	private String sign;
    
    @Schema(description = "交易哈希")
	private String hash;
    
    @Schema(description = "请求订单号")
	private String requestOrderNum;
    
    @Schema(description = "状态码")
	private Integer stateCode;
    
}
