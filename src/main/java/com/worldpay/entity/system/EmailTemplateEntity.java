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
@TableName("email_template")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class EmailTemplateEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("template_number")
	@Schema(description = "")
private Integer templateNumber;
	
	@TableField("language")
	@Schema(description = "")
private String language;
	
	@TableField("language_name")
	@Schema(description = "")
private String languageName;
	
	@TableField("template_name")
	@Schema(description = "")
private String templateName;
	
	@TableField("template_subject")
	@Schema(description = "")
private String templateSubject;
	
	@TableField("template_content")
	@Schema(description = "")
private String templateContent;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
}
