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
@TableName("help_data")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class HelpEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("language")
	@Schema(description = "")
private String language;
	
	@TableField("language_name")
	@Schema(description = "")
private String languageName;
	
	@TableField("article_name")
	@Schema(description = "")
private String articleName;
	
	@TableField("article_url")
	@Schema(description = "")
private String articleUrl;
	
	@TableField("article_content")
	@Schema(description = "")
private String articleContent;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
}
