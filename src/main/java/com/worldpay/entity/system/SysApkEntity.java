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
@Schema(description = "APK版本信息")
public class SysApkEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("platform")
	@Schema(description = "平台类型")
	private Integer platform;
	
	@TableField("app_name")
	@Schema(description = "应用名称")
	private String appName;
	
	@TableField("state")
	@Schema(description = "状态")
	private Integer state;
	
	@TableField("apk_url")
	@Schema(description = "安装包下载地址")
	private String apkUrl;
	
	@TableField("update_type")
	@Schema(description = "更新类型")
	private Integer updateType;
	
	@TableField("app_version")
	@Schema(description = "应用版本号")
	private String appVersion;

	@TableField("version_code")
	@Schema(description = "版本编码")
	private String versionCode;
	
	@TableField("update_content_zh")
	@Schema(description = "中文更新内容")
	private String updateContentZh;
	
	@TableField("update_content_en")
	@Schema(description = "英文更新内容")
	private String updateContentEn;
	
	@TableField("update_content_tw")
	@Schema(description = "繁体更新内容")
	private String updateContentTw;
	
	@TableField("set_user")
	@Schema(description = "设置人ID")
	private Integer setUser;
	
	@TableField("set_user_name")
	@Schema(description = "设置人名称")
	private String setUserName;
	
	@TableField("set_time")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmt_modified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
