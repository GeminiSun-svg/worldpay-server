package com.worldpay.entity.merchant;

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
/**
 * @category 商户充值记录
 * @author Hlin
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("merchants_recharge_history")
@Schema(description = "")
public class RechargeHistoryEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("amount")
	@Schema(description = "")
private Double amount;
	
	@TableField("coinAmount")
	@Schema(description = "")
private Double coinAmount;
	
	@TableField("createAt")
	@Schema(description = "")
private Long createAt;

	@TableField("currency")
	@Schema(description = "")
private String currency;
	
	@TableField("orderNo")
	@Schema(description = "")
private String orderNo;
	
	@TableField("receiveAmount")
	@Schema(description = "")
private Double receiveAmount;
	
	@TableField("status")
	@Schema(description = "")
private String status;
	
	@TableField("symbol")
	@Schema(description = "")
private String symbol;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

	@TableField(exist = false)
	@Schema(description = "")
private BigDecimal availableAmount;
	
	@TableField(exist = false)
	@Schema(description = "")
private String begTime;
	
	@TableField(exist = false)
	@Schema(description = "")
private String endTime;
	
	@TableField(exist = false)
	@Schema(description = "")
private BigDecimal frozenAmount;
}
