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
@Schema(description = "")
public class AppUserCardOpenEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("order_num")
	@Schema(description = "")
private String orderNum;
	
	@TableField("user_bankcard_id")
	@Schema(description = "")
private Integer userBankcardId;
	
//	@TableField("card_no")
//	@Schema(description = "")
//	private String cardNo;
	
	@TableField("open_card_amount")
	@Schema(description = "")
private Double openCardAmount;
	
	@TableField("open_state")
	@Schema(description = "")
private Integer openState;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
