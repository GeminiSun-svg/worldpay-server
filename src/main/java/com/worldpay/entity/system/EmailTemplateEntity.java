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
@Schema(description = "邮件模板")
public class EmailTemplateEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("template_number")
	@Schema(description = "模板编号")
	private Integer templateNumber;
	
	@TableField("language")
	@Schema(description = "语言编码")
	private String language;
	
	@TableField("language_name")
	@Schema(description = "语言名称")
	private String languageName;
	
	@TableField("template_name")
	@Schema(description = "模板名称")
	private String templateName;
	
	@TableField("template_subject")
	@Schema(description = "邮件主题")
	private String templateSubject;
	
	@TableField("template_content")
	@Schema(description = "模板内容")
	private String templateContent;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
}
