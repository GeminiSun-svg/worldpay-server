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
@EqualsAndHashCode(callSuper = true)
@TableName("finance_product")
@Schema(description = "")
public class ProductEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("language")
	@Schema(description = "")
private String language;
	
	@TableField("cover_img")
	@Schema(description = "")
private String coverImg;
	
	@TableField("product_name")
	@Schema(description = "")
private String productName;
	
	@TableField("product_info")
	@Schema(description = "")
private String productInfo;
	
	@TableField("product_type_id")
	@Schema(description = "")
private Integer productTypeId;
	
	@TableField("product_type")
	@Schema(description = "")
private String productType;
	
	@TableField("funds_type")
	@Schema(description = "")
private String fundsType;
	
	@TableField("product_state_id")
	@Schema(description = "")
private Integer productStateId;
	
	@TableField("product_state")
	@Schema(description = "")
private String productState;
	
	@TableField("initial_monery")
	@Schema(description = "")
private Double initialMonery;
	
	@TableField("product_time")
	@Schema(description = "")
private Integer productTime;
	
	@TableField("interest_rate")
	@Schema(description = "")
private Double interestRate;
	
	@TableField("whether_current")
	@Schema(description = "")
private Integer whetherCurrent;

	@TableField("delete_state")
	@Schema(description = "")
private Integer deleteState;
	
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
