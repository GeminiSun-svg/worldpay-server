package com.worldpay.entity.system;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("sys_state")
@Schema(description = "系统状态")
public class SysStateEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("wallet_state")
	@Schema(description = "钱包开关状态")
	private Boolean walletState;
	
	@TableField("operate_user")
	@Schema(description = "操作用户")
	private String operateUser;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
