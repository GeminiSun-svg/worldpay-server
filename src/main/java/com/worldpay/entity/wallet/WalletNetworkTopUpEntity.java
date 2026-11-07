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
@Schema(description = "")
public class WalletNetworkTopUpEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;

	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("user_email")
	@Schema(description = "")
private String userEmail;
	
	@TableField("user_tel")
	@Schema(description = "")
private String userTel;

	@TableField("order_no")
	@Schema(description = "")
private String orderNo;
	
	@TableField("network_type")
	@Schema(description = "")
private String networkType;
	
	@TableField("wallet_address")
	@Schema(description = "")
private String walletAddress;
	
	@TableField("trade_hash")
	@Schema(description = "")
private String tradeHash;
	
    @TableField("topup_Amount")
    @Schema(description = "")
private BigDecimal topupAmount;
    
    @TableField("order_remark")
    @Schema(description = "")
private String orderRemark;
    
    @TableField("review_remark")
    @Schema(description = "")
private String reviewRemark;
    
    @TableField("order_state")
    @Schema(description = "")
private Integer orderState;
    
    @TableField("order_state_name")
    @Schema(description = "")
private String orderStateName;
    
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private Double walletBalance;

}
