package com.worldpay.entity.merchant;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("merchant_data")
@Schema(description = "商户资产信息")
public class MerchantEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("address")
	@Schema(description = "商户钱包地址")
	private String address;
	
	@TableField("network")
	@Schema(description = "网络名称")
	private String network;
	
	@TableField("symbol")
	@Schema(description = "币种符号")
	private String symbol;
	
	@TableField("currency")
	@Schema(description = "币种")
	private String currency;
	
	@TableField("available_amount")
	@Schema(description = "可用余额")
	private BigDecimal availableAmount;
	
	@TableField("frozen_amount")
	@Schema(description = "冻结金额")
	private BigDecimal frozenAmount;
}
