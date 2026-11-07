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
@Schema(description = "")
public class WithdrawalRecordEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Long id;

    @TableField("app_id")
    @Schema(description = "")
private Integer appId;

    @TableField("app_uid")
    @Schema(description = "")
private String appUid;

    @TableField("user_email")
    @Schema(description = "")
private String userEmail;

    @TableField("before_withdrawal_wallet_balance")
    @Schema(description = "")
private Double beforeWithdrawalWalletBalance;

    @TableField("after_withdrawal_wallet_balance")
    @Schema(description = "")
private Double afterWithdrawalWalletBalance;

    @TableField("withdrawal_wallet_balance")
    @Schema(description = "")
private Double withdrawalWalletAmount;

    @TableField("setTime")
    @Schema(description = "")
private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;
}