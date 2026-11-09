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
@Schema(description = "iOS钱包入口状态")
public class AppUserIosWalletStateEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("app_user_id")
	@Schema(description = "App用户ID")
	private Integer appUserId;
	
	@TableField("app_user_uid")
	@Schema(description = "用户ID")
	private String appUserUid;
	
	@TableField("wallet_state")
	@Schema(description = "钱包入口状态")
	private Integer walletState;
	
	@TableField("topUp_state")
	@Schema(description = "充值入口状态")
	private Integer topUpState;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
