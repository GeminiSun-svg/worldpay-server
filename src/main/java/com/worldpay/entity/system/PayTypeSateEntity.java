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
@Schema(description = "支付方式开关")
public class PayTypeSateEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("ios_pay")
	@Schema(description = "是否开启苹果支付")
	private boolean iosPay;
	
	@TableField("usdt_pay")
	@Schema(description = "是否开启USDT支付")
	private boolean usdtPay;
	
	@TableField("card_pay")
	@Schema(description = "是否开启银行卡支付")
	private boolean cardPay;
	
	@TableField("manage_state")
	@Schema(description = "管理端开关状态")
	private boolean manageState;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
