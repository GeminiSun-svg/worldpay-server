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
@TableName("app_user_intiter")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")

public class AppUserIntiterEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")

    private Integer id;
	
	@TableField("intiter_user_type")
	@Schema(description = "")

    private Integer intiterUserType;
	
	@TableField("intiter_user_level")
	@Schema(description = "")

    private Integer intiterUserLevel;
	
	@TableField("inviter_user")
	@Schema(description = "")

    private Integer InviterUser;
	
	@TableField("inviter_user_uid")
	@Schema(description = "")

    private String InviterUserUid;
	
	@TableField("cover_inviter_user")
	@Schema(description = "")

    private Integer coverInviterUser;
	
	@TableField("cover_inviter_user_uid")
	@Schema(description = "")

    private String coverInviterUserUid;
	
	@TableField("setTime")
	@Schema(description = "")

    private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")

	private Date gmtModified;

	@TableField(exist = false)
	private String email;

	
}
