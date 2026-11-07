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
@Schema(description = "")
public class WalletTransfetRatesEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;

	@TableField("rates")
	@Schema(description = "")
private Double rates;
	
	@TableField("set_user")
	@Schema(description = "")
private Integer setUser;
	
	@TableField("set_user_name")
	@Schema(description = "")
private String setUserName;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
