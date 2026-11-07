package com.worldpay.entity.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("channel_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class ChannelUserEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;

	@TableField("user_id")
	@Schema(description = "")
private Integer userId;

	@TableField("user_name")
	@Schema(description = "")
private String username;

	@TableField("level_no")
	@Schema(description = "")
private Integer levelNo;

	@TableField("delete_status")
	@Schema(description = "")
private Integer deleteStatus;

	@TableField("level_name")
	@Schema(description = "")
private String levelName;

	@TableField("invitation_code")
	@Schema(description = "")
private String invitationCode;

	@TableField("commission_rate")
	@Schema(description = "")
private Double commissionRate;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

	@TableField("card_opening_deduction_fee")
	@Schema(description = "")
private Double cardOpeningDeductionFee;

	@TableField(exist = false)
	private String tel;

	@TableField(exist = false)
	private String acctive;

	@TableField(exist = false)
	private String googleSecretkey;

	@TableField(exist = false)
	private String ipWhite;
}
