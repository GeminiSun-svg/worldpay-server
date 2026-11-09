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
@Schema(description = "钱包转出记录")
public class WalletToWebLogEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;

	@TableField("order_no")
	@Schema(description = "订单号")
	private String orderNo;

	@TableField("user_email")
	@Schema(description = "用户邮箱")
	private String userEmail;

	@TableField("user_tel")
	@Schema(description = "用户手机号")
	private String userTel;
	
	@TableField("network_type")
	@Schema(description = "网络类型")
	private String networkType;
	
	@TableField("token_name")
	@Schema(description = "代币名称")
	private String tokenName;
	
	@TableField("token_address")
	@Schema(description = "代币地址")
	private String tokenAddress;

	@TableField("wallet_address")
	@Schema(description = "钱包地址")
	private String walletAddress;

	@TableField("transfer_amount")
	@Schema(description = "转账金额")
	private Double transferAmount;
	
	@TableField("server_amount")
	@Schema(description = "服务费")
	private Double serverAmount;
	
	@TableField("handling_fee")
	@Schema(description = "手续费")
	private Double handlingFee;
	
	@TableField("real_amount")
	@Schema(description = "实际到账金额")
	private Double realAmount;

	@TableField("apply_state")
	@Schema(description = "申请状态")
	private Integer applyState;
	
	@TableField("apply_state_name")
	@Schema(description = "申请状态名称")
	private String applyStateName;
	
	@TableField("reject_content")
	@Schema(description = "驳回原因")
	private String rejectContent;
	
	@TableField("tran_hash")
	@Schema(description = "交易哈希")
	private String tranHash;
	
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
