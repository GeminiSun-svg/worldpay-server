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
@Schema(description = "用户邀请关系")

public class AppUserIntiterEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")

    private Integer id;
	
	@TableField("intiter_user_type")
	@Schema(description = "邀请人类型")

    private Integer intiterUserType;
	
	@TableField("intiter_user_level")
	@Schema(description = "邀请人层级")

    private Integer intiterUserLevel;
	
	@TableField("inviter_user")
	@Schema(description = "邀请人ID")

    private Integer InviterUser;
	
	@TableField("inviter_user_uid")
	@Schema(description = "邀请人用户ID")

    private String InviterUserUid;
	
	@TableField("cover_inviter_user")
	@Schema(description = "被邀请人ID")

    private Integer coverInviterUser;
	
	@TableField("cover_inviter_user_uid")
	@Schema(description = "被邀请人用户ID")

    private String coverInviterUserUid;
	
	@TableField("setTime")
	@Schema(description = "创建时间")

    private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")

	private Date gmtModified;

	@TableField(exist = false)
	@Schema(description = "邮箱")
	private String email;

	
}
