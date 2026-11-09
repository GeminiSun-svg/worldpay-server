package com.worldpay.entity.wallet;

import java.math.BigDecimal;
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
@TableName("wallet_network_topup")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "链上充值记录")
public class WalletNetworkTopUpEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("user_email")
	@Schema(description = "用户邮箱")
	private String userEmail;
	
	@TableField("user_tel")
	@Schema(description = "用户手机号")
	private String userTel;

	@TableField("order_no")
	@Schema(description = "订单号")
	private String orderNo;
	
	@TableField("network_type")
	@Schema(description = "网络类型")
	private String networkType;
	
	@TableField("wallet_address")
	@Schema(description = "钱包地址")
	private String walletAddress;
	
	@TableField("trade_hash")
	@Schema(description = "交易哈希")
	private String tradeHash;
	
	@TableField("topup_Amount")
	@Schema(description = "充值金额")
	private BigDecimal topupAmount;
    
	@TableField("order_remark")
	@Schema(description = "订单备注")
	private String orderRemark;
    
	@TableField("review_remark")
	@Schema(description = "审核备注")
	private String reviewRemark;
    
	@TableField("order_state")
	@Schema(description = "订单状态")
	private Integer orderState;
    
	@TableField("order_state_name")
	@Schema(description = "订单状态名称")
	private String orderStateName;
    
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "钱包余额")
	private Double walletBalance;

}
