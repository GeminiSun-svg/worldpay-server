package com.worldpay.entity.appuser;
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
@EqualsAndHashCode(callSuper = true)
@TableName("app_user_card_open")
@Schema(description = "用户开卡记录")
public class AppUserCardOpenEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("order_num")
	@Schema(description = "订单号")
	private String orderNum;
	
	@TableField("user_bankcard_id")
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
	
//	@TableField("card_no")
//	@Schema(description = "")
//	private String cardNo;
	
	@TableField("open_card_amount")
	@Schema(description = "开卡金额")
	private Double openCardAmount;
	
	@TableField("open_state")
	@Schema(description = "开卡状态")
	private Integer openState;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
