package com.worldpay.entity.system;

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
@TableName("pay_type_sate")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class PayTypeSateEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("ios_pay")
	@Schema(description = "")
private boolean iosPay;
	
	@TableField("usdt_pay")
	@Schema(description = "")
private boolean usdtPay;
	
	@TableField("card_pay")
	@Schema(description = "")
private boolean cardPay;
	
	@TableField("manage_state")
	@Schema(description = "")
private boolean manageState;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
