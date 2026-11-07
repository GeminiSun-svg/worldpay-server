package com.worldpay.entity.merchant;

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
@EqualsAndHashCode(callSuper = true)
@TableName("merchant_trade_fee")
@Schema(description = "")
public class MerchantTradeFeeEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("amount")
	@Schema(description = "")
private Double amount;
	
	@TableField("createAt")
	@Schema(description = "")
private Long createAt;
	
	@TableField("currency")
	@Schema(description = "")
private String currency;
	
	@TableField("orderNo")
	@Schema(description = "")
private String orderNo;
	
	@TableField("transactionId")
	@Schema(description = "")
private String transactionId;
	
	@TableField("userBankcardId")
	@Schema(description = "")
private Integer userBankcardId;
	
	@TableField("cardNum")
	@Schema(description = "")
private String cardNum;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
