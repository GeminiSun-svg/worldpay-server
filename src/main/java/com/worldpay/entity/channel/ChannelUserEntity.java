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
@Schema(description = "渠道用户")
public class ChannelUserEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

	@TableField("user_id")
	@Schema(description = "系统用户ID")
	private Integer userId;

	@TableField("user_name")
	@Schema(description = "用户名")
	private String username;

	@TableField("level_no")
	@Schema(description = "层级编号")
	private Integer levelNo;

	@TableField("delete_status")
	@Schema(description = "删除状态")
	private Integer deleteStatus;

	@TableField("level_name")
	@Schema(description = "层级名称")
	private String levelName;

	@TableField("invitation_code")
	@Schema(description = "邀请码")
	private String invitationCode;

	@TableField("commission_rate")
	@Schema(description = "佣金费率")
	private Double commissionRate;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

	@TableField("card_opening_deduction_fee")
	@Schema(description = "开卡抵扣手续费")
	private Double cardOpeningDeductionFee;

	@TableField(exist = false)
	@Schema(description = "手机号")
	private String tel;

	@TableField(exist = false)
	@Schema(description = "登录账号")
	private String acctive;

	@TableField(exist = false)
	@Schema(description = "谷歌验证密钥")
	private String googleSecretkey;

	@TableField(exist = false)
	@Schema(description = "IP白名单")
	private String ipWhite;
}
