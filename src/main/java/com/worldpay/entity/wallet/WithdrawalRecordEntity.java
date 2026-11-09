package com.worldpay.entity.wallet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：提现记录表
 *
 * @author GeminiSun
 * @date 2026/05/19 16:06
 */
@Data
@TableName("withdrawal_record")
@Schema(description = "提现记录")
public class WithdrawalRecordEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Long id;

    @TableField("app_id")
    @Schema(description = "应用ID")
	private Integer appId;

    @TableField("app_uid")
    @Schema(description = "用户ID")
	private String appUid;

    @TableField("user_email")
    @Schema(description = "用户邮箱")
	private String userEmail;

    @TableField("before_withdrawal_wallet_balance")
    @Schema(description = "提现前钱包余额")
	private Double beforeWithdrawalWalletBalance;

    @TableField("after_withdrawal_wallet_balance")
    @Schema(description = "提现后钱包余额")
	private Double afterWithdrawalWalletBalance;

    @TableField("withdrawal_wallet_balance")
    @Schema(description = "提现金额")
	private Double withdrawalWalletAmount;

    @TableField("setTime")
    @Schema(description = "创建时间")
	private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")
	private Date gmtModified;
}