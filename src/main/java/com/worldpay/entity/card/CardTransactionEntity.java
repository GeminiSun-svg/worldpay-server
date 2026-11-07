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
@TableName("card_transaction")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class CardTransactionEntity extends PageQueryHelperEntity{
	
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
	
	@TableField("user_bankcard_id")
	@Schema(description = "")
private Integer userBankcardId;
	
	@TableField("card_no")
	@Schema(description = "")
private String cardNo;
	
	@TableField("handling_fees")
	@Schema(description = "")
private Double handlingFees;
	
	@TableField("local_currency")
	@Schema(description = "")
private String localCurrency;
    
	@TableField("local_currency_amt")
	@Schema(description = "")
private String localCurrencyAmt;
	
	@TableField("trans_currency")
	@Schema(description = "")
private String transCurrency;
    
	@TableField("trans_currency_amt")
	@Schema(description = "")
private String transCurrencyAmt;
	
	@TableField("request_orderid")
	@Schema(description = "")
private String requestOrderId;
	
	@TableField("order_state")
	@Schema(description = "")
private String orderState;
	
	@TableField("pay_type")
	@Schema(description = "")
private Integer payType;
	
	@TableField("trans_type")
	@Schema(description = "")
private String transType;
	
	@TableField("create_at")
	@Schema(description = "")
private Date createAt;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField("setUser")
	@Schema(description = "")
private Integer setUser;
	
	@TableField("setUserName")
	@Schema(description = "")
private String setUserName;
	
	@TableField("title")
	@Schema(description = "")
private String title;
	
	@TableField(exist = false)
	@Schema(description = "")
private String userEmail;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardEntity cardData;
	
	@TableField(exist = false)
	@Schema(description = "")
private String orderStateName;
	
	@TableField(exist = false)
	@Schema(description = "")
private String totalManey;

}
