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
@Schema(description = "卡片邮寄信息")
public class CardShippingEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("apply_id")
	@Schema(description = "申请ID")
	private Integer applyId;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("user_email")
	@Schema(description = "用户邮箱")
	private String userEmail;
	
	@TableField("user_name")
	@Schema(description = "用户名称")
	private String userName;
	
	@TableField("user_tel")
	@Schema(description = "用户手机号")
	private String userTel;
	
	@TableField("logistics_providers")
	@Schema(description = "物流服务商")
	private String logisticsProviders;
	
	@TableField("logistics_num")
	@Schema(description = "物流单号")
	private String logisticsNum;
	
	@TableField("logistics_monery")
	@Schema(description = "物流费用")
	private Double logisticsMonery;
	
	@TableField("logistics_state")
	@Schema(description = "物流状态")
	private Integer logisticsState;
	
	@TableField("logistics_state_name")
	@Schema(description = "物流状态名称")
	private String logisticsStateName;
	
	@TableField("operate_user_id")
	@Schema(description = "操作人ID")
	private Integer operateUserId;

	@TableField("operate_user_name")
	@Schema(description = "操作人名称")
	private String operateUserName;

	@TableField("operate_user_ip")
	@Schema(description = "操作人IP")
	private String operateUserIp;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
}
