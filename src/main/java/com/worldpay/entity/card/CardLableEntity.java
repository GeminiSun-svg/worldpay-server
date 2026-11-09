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
@TableName("card_lable")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "卡片标签")
public class CardLableEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("language")
	@NotBlank(message = "语言分类不能为空")
	@Schema(description = "语言")
	private String language;
	
	@TableField("name")
	@NotBlank(message = "标签名称不能为空")
	@Schema(description = "标签名称")
	private String name;
	
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
