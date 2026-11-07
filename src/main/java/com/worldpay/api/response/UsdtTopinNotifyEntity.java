package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * USDT充值回调
 */
@Data
public class UsdtTopinNotifyEntity {
	
    @Schema(description = "")
private String type;
    
    @Schema(description = "")
private String block;
   
    @Schema(description = "")
private String uid;
    
    @Schema(description = "")
private String email;

    @Schema(description = "")
private String orderNo;
    
    @Schema(description = "")
private String chain;

    @Schema(description = "")
private String coin;

    @Schema(description = "")
private String amount;
    
    @Schema(description = "")
private String amount_raw;
    
    @Schema(description = "")
private String time;

    @Schema(description = "")
private String outaddress;

    @Schema(description = "")
private String inaddress;

    @Schema(description = "")
private String sign;
    
    @Schema(description = "")
private String hash;
    
    @Schema(description = "")
private String requestOrderNum;
    
    @Schema(description = "")
private Integer stateCode;
    
}
