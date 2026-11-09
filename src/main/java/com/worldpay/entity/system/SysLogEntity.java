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
@Schema(description = "系统日志")
public class SysLogEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Long id;
	
	@TableField("userName")
	@Schema(description = "用户名")
	private String userName;
	
	@TableField("moduleName")
	@Schema(description = "模块名称")
	private String moduleName;
	
	@TableField("moduleInfo")
	@Schema(description = "模块信息")
	private String moduleInfo;
	
	@TableField("reqType")
	@Schema(description = "请求类型")
	private String reqType;
	
	@TableField("reqUrl")
	@Schema(description = "请求地址")
	private String reqUrl;
	
	@TableField("ipUrl")
	@Schema(description = "IP地址")
	private String ipUrl;
	
	@TableField("reqParamet")
	@Schema(description = "请求参数")
	private String reqParamet;
	
	@TableField("resData")
	@Schema(description = "响应数据")
	private String resData;
	
	@TableField("begTime")
	@Schema(description = "开始时间")
	private Date begTime;
	
	@TableField("endTime")
	@Schema(description = "结束时间")
	private Date endTime;
	
	

}
