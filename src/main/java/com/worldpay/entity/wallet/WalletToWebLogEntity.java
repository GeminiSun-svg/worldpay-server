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
@TableName("wallet_to_web_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class WalletToWebLogEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;

	@TableField("order_no")
	@Schema(description = "")
private String orderNo;

	@TableField("user_email")
	@Schema(description = "")
private String userEmail;

	@TableField("user_tel")
	@Schema(description = "")
private String userTel;
	
	@TableField("network_type")
	@Schema(description = "")
private String networkType;
	
	@TableField("token_name")
	@Schema(description = "")
private String tokenName;
	
	@TableField("token_address")
	@Schema(description = "")
private String tokenAddress;

	@TableField("wallet_address")
	@Schema(description = "")
private String walletAddress;

	@TableField("transfer_amount")
	@Schema(description = "")
private Double transferAmount;
	
	@TableField("server_amount")
	@Schema(description = "")
private Double serverAmount;
	
	@TableField("handling_fee")
	@Schema(description = "")
private Double handlingFee;
	
	@TableField("real_amount")
	@Schema(description = "")
private Double realAmount;

	@TableField("apply_state")
	@Schema(description = "")
private Integer applyState;
	
	@TableField("apply_state_name")
	@Schema(description = "")
private String applyStateName;
	
	@TableField("reject_content")
	@Schema(description = "")
private String rejectContent;
	
	@TableField("tran_hash")
	@Schema(description = "")
private String tranHash;
	
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
