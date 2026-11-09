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
@TableName("message_template")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息模板")
public class MessageTemplateEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("message_code")
	@Schema(description = "消息编码")
	private Integer messageCode;
	
	@TableField("language")
	@Schema(description = "语言编码")
	private String language;
	
	@TableField("language_name")
	@Schema(description = "语言名称")
	private String languageName;
	
	@TableField("msg_titles")
	@Schema(description = "消息标题")
	private String msgTitles;
	
	@TableField("message_info")
	@Schema(description = "消息内容")
	private String messageInfo;
	
	@TableField("message_url")
	@Schema(description = "消息链接")
	private String messageUrl;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
