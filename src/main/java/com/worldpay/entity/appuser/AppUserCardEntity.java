package com.worldpay.entity.appuser;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.entity.card.CardApplyManEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_user_card")
@Schema(description = "App用户卡片")
public class AppUserCardEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("card_id")
	@Schema(description = "卡片ID")
	private Integer cardId;
	
	@TableField("card_uuid")
	@Schema(description = "卡片唯一标识")
	private String cardUuid;

	@TableField("apply_id")
	@Schema(description = "开卡申请ID")
	private Integer applyId;
	
	@TableField("card_type")
	@Schema(description = "卡片类型")
	private String cardType;
	
	@TableField("card_no")
	@Schema(description = "卡号")
	private String cardNo;
	
	@TableField("user_bankcard_id")
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@TableField("status")
	@Schema(description = "卡片状态")
	private String status;
	
	@TableField("balance")
	@Schema(description = "卡片余额")
	private Double balance;
	
	@TableField("tag")
	@Schema(description = "标签")
	private String tag;
	
	@TableField("shipping_state")
	@Schema(description = "邮寄状态")
	private Integer shippingState;
	
	@TableField("shipping_state_name")
	@Schema(description = "邮寄状态名称")
	private String shippingStateName;
	
	@TableField("shipping_time")
	@Schema(description = "邮寄时间")
	private Date shippingTime;
	
	@TableField("logistics_num")
	@Schema(description = "物流单号")
	private String logisticsNum;

	@TableField("pin_num")
	@Schema(description = "PIN密码")
	private String pinNum;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "卡片信息")
	private CardEntity cardData;
	
	@TableField(exist = false)
	@Schema(description = "物流信息")
	private Object logisticsInfo;
	
	@TableField(exist = false)
	@Schema(description = "持卡人信息")
	private AppUserHolderEntity holderData;
	
	@TableField(exist = false)
	@Schema(description = "申请人信息")
	private CardApplyManEntity manData;
	
	@TableField(exist = false)
	@Schema(description = "用户邮箱")
	private String userEmail;
	
	@TableField(exist = false)
	@Schema(description = "用户手机号")
	private String userTel;
	
	@TableField(exist = false)
	@Schema(description = "用户名称")
	private String userName;
	
	@TableField(exist = false)
	@Schema(description = "状态ID")
	private Integer statusId;
	
	@TableField(exist = false)
	@Schema(description = "状态名称")
	private String stateName;

}
