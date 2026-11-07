package com.worldpay.entity.wallet;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("wallet_transfet_list")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class WalletTransfetListEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("order_no")
    @Schema(description = "")
private String orderNo;

	@TableField("send_uid")
	@Schema(description = "")
private String sendUid;
	
	@TableField("send_email")
	@Schema(description = "")
private String sendEmail;
	
	@TableField("send_forward_balance")
	@Schema(description = "")
private Double sendForwardBalance;
	
	@TableField("send_back_balance")
	@Schema(description = "")
private Double sendBackBalance;
	
	@TableField("recipient_uid")
	@Schema(description = "")
private String recipientUid;
	
	@TableField("recipient_email")
	@Schema(description = "")
private String recipientEmail;
	
	@TableField("recipient_forward_balance")
	@Schema(description = "")
private Double recipientForwardBalance;
	
	@TableField("recipient_back_balance")
	@Schema(description = "")
private Double recipientBackBalance;
	
	@TableField("send_money")
	@Schema(description = "")
private Double sendMoney;
	
	@TableField("send_rates")
	@Schema(description = "")
private Double sendRates;
	
	@TableField("actual_money")
	@Schema(description = "")
private Double actualMoney;
	
	@TableField("handling_fee")
	@Schema(description = "")
private Double handlingFee;
	
    @TableField("setTime")
    @Schema(description = "")
private Date setTime;
    
    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;
    
    @TableField(exist = false)
    @Schema(description = "")
private String payPassword;

}
