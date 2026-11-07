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
@Schema(description = "")
public class SysStateEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("wallet_state")
	@Schema(description = "")
private Boolean walletState;
	
	@TableField("operate_user")
	@Schema(description = "")
private String operateUser;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
