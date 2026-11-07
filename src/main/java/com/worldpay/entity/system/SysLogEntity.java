package com.worldpay.entity.system;

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
@TableName("sys_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class SysLogEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Long id;
	
	@TableField("userName")
	@Schema(description = "")
private String userName;
	
	@TableField("moduleName")
	@Schema(description = "")
private String moduleName;
	
	@TableField("moduleInfo")
	@Schema(description = "")
private String moduleInfo;
	
	@TableField("reqType")
	@Schema(description = "")
private String reqType;
	
	@TableField("reqUrl")
	@Schema(description = "")
private String reqUrl;
	
	@TableField("ipUrl")
	@Schema(description = "")
private String ipUrl;
	
	@TableField("reqParamet")
	@Schema(description = "")
private String reqParamet;
	
	@TableField("resData")
	@Schema(description = "")
private String resData;
	
	@TableField("begTime")
	@Schema(description = "")
private Date begTime;
	
	@TableField("endTime")
	@Schema(description = "")
private Date endTime;
	
	

}
