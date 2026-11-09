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
@Schema(description = "商户充值记录")
public class RechargeHistoryEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("amount")
	@Schema(description = "充值金额")
	private Double amount;
	
	@TableField("coinAmount")
	@Schema(description = "币种金额")
	private Double coinAmount;
	
	@TableField("createAt")
	@Schema(description = "创建时间戳")
	private Long createAt;

	@TableField("currency")
	@Schema(description = "币种")
	private String currency;
	
	@TableField("orderNo")
	@Schema(description = "订单号")
	private String orderNo;
	
	@TableField("receiveAmount")
	@Schema(description = "到账金额")
	private Double receiveAmount;
	
	@TableField("status")
	@Schema(description = "状态")
	private String status;
	
	@TableField("symbol")
	@Schema(description = "币种符号")
	private String symbol;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

	@TableField(exist = false)
	@Schema(description = "可用余额")
	private BigDecimal availableAmount;
	
	@TableField(exist = false)
	@Schema(description = "开始时间")
	private String begTime;
	
	@TableField(exist = false)
	@Schema(description = "结束时间")
	private String endTime;
	
	@TableField(exist = false)
	@Schema(description = "冻结金额")
	private BigDecimal frozenAmount;
}
