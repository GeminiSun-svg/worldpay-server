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
@TableName("sys_apk")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class SysApkEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("platform")
	@Schema(description = "")
private Integer platform;
	
	@TableField("app_name")
	@Schema(description = "")
private String appName;
	
	@TableField("state")
	@Schema(description = "")
private Integer state;
	
	@TableField("apk_url")
	@Schema(description = "")
private String apkUrl;
	
	@TableField("update_type")
	@Schema(description = "")
private Integer updateType;
	
	@TableField("app_version")
	@Schema(description = "")
private String appVersion;

	@TableField("version_code")
	@Schema(description = "")
private String versionCode;
	
	@TableField("update_content_zh")
	@Schema(description = "")
private String updateContentZh;
	
	@TableField("update_content_en")
	@Schema(description = "")
private String updateContentEn;
	
	@TableField("update_content_tw")
	@Schema(description = "")
private String updateContentTw;
	
	@TableField("set_user")
	@Schema(description = "")
private Integer setUser;
	
	@TableField("set_user_name")
	@Schema(description = "")
private String setUserName;
	
	@TableField("set_time")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmt_modified")
	@Schema(description = "")
private Date gmtModified;

}
