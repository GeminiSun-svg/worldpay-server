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
@Schema(description = "理财产品类型")
public class ProductTypeEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("language")
	@Schema(description = "语言")
	private String language;
	
	@TableField("name")
	@Schema(description = "类型名称")
	private String name;
	
	@TableField("whether_current")
	@Schema(description = "是否活期")
	private Integer whetherCurrent;
	
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
