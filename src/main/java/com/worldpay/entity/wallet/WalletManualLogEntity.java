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
@TableName("wallet_manual_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class WalletManualLogEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;

	@TableField("order_no")
	@Schema(description = "")
private String orderNo;

	@TableField("user_id")
	@Schema(description = "")
private Integer userId;

	@TableField("uid")
	@Schema(description = "")
private String uid;

	@TableField("user_email")
	@Schema(description = "")
private String userEmail;

	@TableField("user_tel")
	@Schema(description = "")
private String userTel;
	
	@TableField("wallet_type")
	@Schema(description = "")
private String walletType;

	@TableField("wallet_address")
	@Schema(description = "")
private String walletAddress;

	@TableField("topup_amount")
	@Schema(description = "")
private Double topupAmount;

	@TableField("topup_amount_forward")
	@Schema(description = "")
private Double topupAmountForward;

	@TableField("topup_amount_back")
	@Schema(description = "")
private Double topupAmountBack;

	@TableField("operate_user_id")
	@Schema(description = "")
private Integer operateUserId;

	@TableField("operate_user_name")
	@Schema(description = "")
private String operateUserName;

	@TableField("operate_user_ip")
	@Schema(description = "")
private String operateUserIp;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private Integer networkTopUpId;

}
