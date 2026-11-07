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
@Schema(description = "")
public class MerchantEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("address")
	@Schema(description = "")
private String address;
	
	@TableField("network")
	@Schema(description = "")
private String network;
	
	@TableField("symbol")
	@Schema(description = "")
private String symbol;
	
	@TableField("currency")
	@Schema(description = "")
private String currency;
	
	@TableField("available_amount")
	@Schema(description = "")
private BigDecimal availableAmount;
	
	@TableField("frozen_amount")
	@Schema(description = "")
private BigDecimal frozenAmount;
}
