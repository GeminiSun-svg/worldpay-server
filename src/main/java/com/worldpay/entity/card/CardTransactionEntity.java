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
@Schema(description = "卡片交易记录")
public class CardTransactionEntity extends PageQueryHelperEntity{
	
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
	
	@TableField("user_bankcard_id")
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@TableField("card_no")
	@Schema(description = "卡号")
	private String cardNo;
	
	@TableField("handling_fees")
	@Schema(description = "手续费")
	private Double handlingFees;
	
	@TableField("local_currency")
	@Schema(description = "本地币种")
	private String localCurrency;
    
	@TableField("local_currency_amt")
	@Schema(description = "本地币种金额")
	private String localCurrencyAmt;
	
	@TableField("trans_currency")
	@Schema(description = "交易币种")
	private String transCurrency;
    
	@TableField("trans_currency_amt")
	@Schema(description = "交易币种金额")
	private String transCurrencyAmt;
	
	@TableField("request_orderid")
	@Schema(description = "请求订单号")
	private String requestOrderId;
	
	@TableField("order_state")
	@Schema(description = "订单状态")
	private String orderState;
	
	@TableField("pay_type")
	@Schema(description = "支付类型")
	private Integer payType;
	
	@TableField("trans_type")
	@Schema(description = "交易类型")
	private String transType;
	
	@TableField("create_at")
	@Schema(description = "业务创建时间")
	private Date createAt;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField("setUser")
	@Schema(description = "创建人ID")
	private Integer setUser;
	
	@TableField("setUserName")
	@Schema(description = "创建人名称")
	private String setUserName;
	
	@TableField("title")
	@Schema(description = "标题")
	private String title;
	
	@TableField(exist = false)
	@Schema(description = "用户邮箱")
	private String userEmail;
	
	@TableField(exist = false)
	@Schema(description = "卡片信息")
	private CardEntity cardData;
	
	@TableField(exist = false)
	@Schema(description = "订单状态名称")
	private String orderStateName;
	
	@TableField(exist = false)
	@Schema(description = "总金额")
	private String totalManey;

}
