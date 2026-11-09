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
@Schema(description = "帮助文档")
public class HelpEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("language")
	@Schema(description = "语言编码")
	private String language;
	
	@TableField("language_name")
	@Schema(description = "语言名称")
	private String languageName;
	
	@TableField("article_name")
	@Schema(description = "文章名称")
	private String articleName;
	
	@TableField("article_url")
	@Schema(description = "文章链接")
	private String articleUrl;
	
	@TableField("article_content")
	@Schema(description = "文章内容")
	private String articleContent;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
}
