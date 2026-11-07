package com.worldpay.entity.product;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("finance_product_order")
@Schema(description = "")
public class ProductOrderEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("product_id")
	@Schema(description = "")
private Integer productId;
	
	@TableField("order_num")
	@Schema(description = "")
private String orderNum;
	
	@TableField("order_state")
	@Schema(description = "")
private String orderState;
	
	@TableField("order_monery")
	@Schema(description = "")
private Double orderMonery;
	
	@TableField("pay_num")
	@Schema(description = "")
private Integer payNum;
	
	@TableField("product_name")
	@Schema(description = "")
private String productName;
	
	@TableField("product_img")
	@Schema(description = "")
private String productImg;
	
	@TableField("product_type")
	@Schema(description = "")
private String productType;
	
	@TableField("funds_type")
	@Schema(description = "")
private String fundsType;
	
	@TableField("product_time")
	@Schema(description = "")
private Integer productTime;
	
	@TableField("interest_rate")
	@Schema(description = "")
private Double interestRate;
	
	@TableField("whether_current")
	@Schema(description = "")
private Integer whetherCurrent;

	@TableField("estimate_income")
	@Schema(description = "")
private Double estimateIncome;
	
	@TableField("actual_income")
	@Schema(description = "")
private Double actualIncome;
	
	@TableField("pay_time")
	@Schema(description = "")
private Date payTime;
	
	@TableField("expire_time")
	@Schema(description = "")
private Date expireTime;
	
	@TableField("finish_time")
	@Schema(description = "")
private Date finishTime;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private AppUserEntity appUser;
	
	@TableField(exist = false)
	@Schema(description = "")
private ProductEntity product;

}
