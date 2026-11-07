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
	@Schema(description = "")
private Long id;
	
	@TableField("name")
	@Schema(description = "")
private String name;
	
	@TableField("dial_code")
	@Schema(description = "")
private String dialCode;
	
	@TableField("code")
	@Schema(description = "")
private String code;
	
	@TableField("language")
	@Schema(description = "")
private String language;

}
