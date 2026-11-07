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
@Schema(description = "")
public class AppUserCardEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("card_id")
	@Schema(description = "")
private Integer cardId;
	
	@TableField("card_uuid")
	@Schema(description = "")
private String cardUuid;

	@TableField("apply_id")
	@Schema(description = "")
private Integer applyId;
	
	@TableField("card_type")
	@Schema(description = "")
private String cardType;
	
	@TableField("card_no")
	@Schema(description = "")
private String cardNo;
	
	@TableField("user_bankcard_id")
	@Schema(description = "")
private Integer userBankcardId;
	
	@TableField("status")
	@Schema(description = "")
private String status;
	
	@TableField("balance")
	@Schema(description = "")
private Double balance;
	
	@TableField("tag")
	@Schema(description = "")
private String tag;
	
	@TableField("shipping_state")
	@Schema(description = "")
private Integer shippingState;
	
	@TableField("shipping_state_name")
	@Schema(description = "")
private String shippingStateName;
	
	@TableField("shipping_time")
	@Schema(description = "")
private Date shippingTime;
	
	@TableField("logistics_num")
	@Schema(description = "")
private String logisticsNum;

	@TableField("pin_num")
	@Schema(description = "")
private String pinNum;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardEntity cardData;
	
	@TableField(exist = false)
	@Schema(description = "")
private Object logisticsInfo;
	
	@TableField(exist = false)
	@Schema(description = "")
private AppUserHolderEntity holderData;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardApplyManEntity manData;
	
	@TableField(exist = false)
	@Schema(description = "")
private String userEmail;
	
	@TableField(exist = false)
	@Schema(description = "")
private String userTel;
	
	@TableField(exist = false)
	@Schema(description = "")
private String userName;
	
	@TableField(exist = false)
	@Schema(description = "")
private Integer statusId;
	
	@TableField(exist = false)
	@Schema(description = "")
private String stateName;

}
