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
@Schema(description = "钱包转账记录")
public class WalletTransfetListEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("order_no")
	@Schema(description = "订单号")
	private String orderNo;

	@TableField("send_uid")
	@Schema(description = "发送方用户ID")
	private String sendUid;
	
	@TableField("send_email")
	@Schema(description = "发送方邮箱")
	private String sendEmail;
	
	@TableField("send_forward_balance")
	@Schema(description = "发送方转账前余额")
	private Double sendForwardBalance;
	
	@TableField("send_back_balance")
	@Schema(description = "发送方转账后余额")
	private Double sendBackBalance;
	
	@TableField("recipient_uid")
	@Schema(description = "接收方用户ID")
	private String recipientUid;
	
	@TableField("recipient_email")
	@Schema(description = "接收方邮箱")
	private String recipientEmail;
	
	@TableField("recipient_forward_balance")
	@Schema(description = "接收方转账前余额")
	private Double recipientForwardBalance;
	
	@TableField("recipient_back_balance")
	@Schema(description = "接收方转账后余额")
	private Double recipientBackBalance;
	
	@TableField("send_money")
	@Schema(description = "转账金额")
	private Double sendMoney;
	
	@TableField("send_rates")
	@Schema(description = "转账费率")
	private Double sendRates;
	
	@TableField("actual_money")
	@Schema(description = "实际到账金额")
	private Double actualMoney;
	
	@TableField("handling_fee")
	@Schema(description = "手续费")
	private Double handlingFee;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
    
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
    
	@TableField(exist = false)
	@Schema(description = "支付密码")
	private String payPassword;

}
