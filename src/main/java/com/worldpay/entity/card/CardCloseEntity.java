package com.worldpay.entity.card;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_close")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class CardCloseEntity extends PageQueryHelperEntity{
	
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

	@TableField("card_type")
	@Schema(description = "")
private String cardType;
	
	@TableField("card_no")
	@Schema(description = "")
private String cardNo;
	
	@TableField("user_bankcard_id")
	@Schema(description = "")
private Integer userBankcardId;
	
	@TableField("balance")
	@Schema(description = "")
private Double balance;
	
	@TableField("refundAmt")
	@Schema(description = "")
private Double refundAmt;
	
	@TableField("request_order_id")
	@Schema(description = "")
private String requestOrderId;
	
	@TableField("review_status")
	@Schema(description = "")
private Integer reviewStatus;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private String payPassword;
	
	@TableField(exist = false)
	@Schema(description = "")
private AppUserEntity appUserData;
	
	@TableField(exist = false)
	@Schema(description = "")
private String reviewStatusName;
}
