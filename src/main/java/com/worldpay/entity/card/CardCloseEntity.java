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
@Schema(description = "卡片注销记录")
public class CardCloseEntity extends PageQueryHelperEntity{
	
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

	@TableField("card_type")
	@Schema(description = "卡片类型")
	private String cardType;
	
	@TableField("card_no")
	@Schema(description = "卡号")
	private String cardNo;
	
	@TableField("user_bankcard_id")
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
	@TableField("balance")
	@Schema(description = "余额")
	private Double balance;
	
	@TableField("refundAmt")
	@Schema(description = "退款金额")
	private Double refundAmt;
	
	@TableField("request_order_id")
	@Schema(description = "请求订单号")
	private String requestOrderId;
	
	@TableField("review_status")
	@Schema(description = "审核状态")
	private Integer reviewStatus;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "支付密码")
	private String payPassword;
	
	@TableField(exist = false)
	@Schema(description = "用户信息")
	private AppUserEntity appUserData;
	
	@TableField(exist = false)
	@Schema(description = "审核状态名称")
	private String reviewStatusName;
}
