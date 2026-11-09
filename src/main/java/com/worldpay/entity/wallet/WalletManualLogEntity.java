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
@Schema(description = "钱包手动充值记录")
public class WalletManualLogEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

	@TableField("order_no")
	@Schema(description = "订单号")
	private String orderNo;

	@TableField("user_id")
	@Schema(description = "用户ID")
	private Integer userId;

	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;

	@TableField("user_email")
	@Schema(description = "用户邮箱")
	private String userEmail;

	@TableField("user_tel")
	@Schema(description = "用户手机号")
	private String userTel;
	
	@TableField("wallet_type")
	@Schema(description = "钱包类型")
	private String walletType;

	@TableField("wallet_address")
	@Schema(description = "钱包地址")
	private String walletAddress;

	@TableField("topup_amount")
	@Schema(description = "充值金额")
	private Double topupAmount;

	@TableField("topup_amount_forward")
	@Schema(description = "充值前余额")
	private Double topupAmountForward;

	@TableField("topup_amount_back")
	@Schema(description = "充值后余额")
	private Double topupAmountBack;

	@TableField("operate_user_id")
	@Schema(description = "操作人ID")
	private Integer operateUserId;

	@TableField("operate_user_name")
	@Schema(description = "操作人名称")
	private String operateUserName;

	@TableField("operate_user_ip")
	@Schema(description = "操作人IP")
	private String operateUserIp;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "链上充值记录ID")
	private Integer networkTopUpId;

}
