package com.worldpay.entity.appuser;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("app_user_uid_join")
@Schema(description = "用户UID变更记录")
public class AppUserUidJoinEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("old_uid")
	@Schema(description = "旧用户ID")
	private String oldUid;
	
	@TableField("new_uid")
	@Schema(description = "新用户ID")
	private String newUid;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
}
