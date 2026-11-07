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
@Schema(description = "")
public class MessageTemplateEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("message_code")
	@Schema(description = "")
private Integer messageCode;
	
	@TableField("language")
	@Schema(description = "")
private String language;
	
	@TableField("language_name")
	@Schema(description = "")
private String languageName;
	
	@TableField("msg_titles")
	@Schema(description = "")
private String msgTitles;
	
	@TableField("message_info")
	@Schema(description = "")
private String messageInfo;
	
	@TableField("message_url")
	@Schema(description = "")
private String messageUrl;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
