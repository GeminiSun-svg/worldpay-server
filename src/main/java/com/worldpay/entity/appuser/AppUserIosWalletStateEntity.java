package com.worldpay.entity.appuser;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("app_user_ios_wallet_state")
@Schema(description = "")
public class AppUserIosWalletStateEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("app_user_id")
	@Schema(description = "")
private Integer appUserId;
	
	@TableField("app_user_uid")
	@Schema(description = "")
private String appUserUid;
	
	@TableField("wallet_state")
	@Schema(description = "")
private Integer walletState;
	
	@TableField("topUp_state")
	@Schema(description = "")
private Integer topUpState;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
