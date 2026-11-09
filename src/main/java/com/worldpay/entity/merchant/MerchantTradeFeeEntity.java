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
@Schema(description = "商户交易手续费记录")
public class MerchantTradeFeeEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("amount")
	@Schema(description = "交易金额")
	private Double amount;
	
	@TableField("createAt")
	@Schema(description = "创建时间戳")
	private Long createAt;
	
	@TableField("currency")
	@Schema(description = "币种")
	private String currency;
	
	@TableField("orderNo")
	@Schema(description = "订单号")
	private String orderNo;
	
	@TableField("transactionId")
	@Schema(description = "交易流水号")
	private String transactionId;
	
	@TableField("userBankcardId")
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@TableField("cardNum")
	@Schema(description = "卡号")
	private String cardNum;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
