package com.worldpay.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("sys_country_code")
public class SyssCountryCodeEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Long id;
	
	@TableField("name")
	@Schema(description = "国家名称")
	private String name;
	
	@TableField("dial_code")
	@Schema(description = "国际区号")
	private String dialCode;
	
	@TableField("code")
	@Schema(description = "国家编码")
	private String code;
	
	@TableField("language")
	@Schema(description = "语言")
	private String language;

}
