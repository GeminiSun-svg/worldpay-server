package com.worldpay.entity.wallet;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("wallet_transfet_rates")
@Schema(description = "钱包转账费率配置")
public class WalletTransfetRatesEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

	@TableField("rates")
	@Schema(description = "转账费率")
	private Double rates;
	
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
