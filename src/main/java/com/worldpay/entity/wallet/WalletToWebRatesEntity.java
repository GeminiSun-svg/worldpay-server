package com.worldpay.entity.wallet;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("wallet_to_web_rates")
@Schema(description = "钱包转出费率配置")

public class WalletToWebRatesEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")

	private Integer id;
	
	@TableField("min_amount")
	@Schema(description = "最小转账金额")

	private Double minAmount;
	
	@TableField("max_amount")
	@Schema(description = "最大转账金额")

	private Double maxAmount;
	
	@TableField("handling_rates")
	@Schema(description = "手续费率")

	private Double handlingRates;
	
	@TableField("server_amount")
	@Schema(description = "服务费")

	private Double serverAmount;
	
	@TableField("set_user")
	@Schema(description = "设置人ID")

	private Integer setUser;
	
	@TableField("set_user_name")
	@Schema(description = "设置人名称")

	private String setUserName;
	
	@TableField("setTime")
	@Schema(description = "创建时间")

	private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "修改时间")

	private Date gmtModified;

}
