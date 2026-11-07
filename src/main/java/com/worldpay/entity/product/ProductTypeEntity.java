package com.worldpay.entity.product;

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
@TableName("finance_product_type")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class ProductTypeEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("language")
	@Schema(description = "")
private String language;
	
	@TableField("name")
	@Schema(description = "")
private String name;
	
	@TableField("whether_current")
	@Schema(description = "")
private Integer whetherCurrent;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private String languageName;

}
