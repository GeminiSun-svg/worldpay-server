package com.worldpay.entity.card;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_synopsis")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "卡片简介")
public class CardSynopsisEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("language")
	@NotBlank(message = "语言不能为空")
	@Schema(description = "语言")
	private String language;
	
	@TableField("title")
	@NotBlank(message = "标题不能为空")
	@Schema(description = "标题")
	private String title;
	
	@TableField("content")
	@NotBlank(message = "内容不能为空")
	@Schema(description = "内容")
	private String content;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "语言名称")
	private String languageName;
	
	
}
