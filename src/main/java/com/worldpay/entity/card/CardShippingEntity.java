package com.worldpay.entity.card;

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
@TableName("card_shipping")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class CardShippingEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("apply_id")
	@Schema(description = "")
private Integer applyId;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("user_email")
	@Schema(description = "")
private String userEmail;
	
	@TableField("user_name")
	@Schema(description = "")
private String userName;
	
	@TableField("user_tel")
	@Schema(description = "")
private String userTel;
	
	@TableField("logistics_providers")
	@Schema(description = "")
private String logisticsProviders;
	
	@TableField("logistics_num")
	@Schema(description = "")
private String logisticsNum;
	
	@TableField("logistics_monery")
	@Schema(description = "")
private Double logisticsMonery;
	
	@TableField("logistics_state")
	@Schema(description = "")
private Integer logisticsState;
	
	@TableField("logistics_state_name")
	@Schema(description = "")
private String logisticsStateName;
	
	@TableField("operate_user_id")
	@Schema(description = "")
private Integer operateUserId;

	@TableField("operate_user_name")
	@Schema(description = "")
private String operateUserName;

	@TableField("operate_user_ip")
	@Schema(description = "")
private String operateUserIp;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
}
