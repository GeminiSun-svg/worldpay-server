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
@Schema(description = "理财产品信息")
public class ProductEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("language")
	@Schema(description = "语言")
	private String language;
	
	@TableField("cover_img")
	@Schema(description = "封面图片")
	private String coverImg;
	
	@TableField("product_name")
	@Schema(description = "产品名称")
	private String productName;
	
	@TableField("product_info")
	@Schema(description = "产品信息")
	private String productInfo;
	
	@TableField("product_type_id")
	@Schema(description = "产品类型ID")
	private Integer productTypeId;
	
	@TableField("product_type")
	@Schema(description = "产品类型")
	private String productType;
	
	@TableField("funds_type")
	@Schema(description = "资金类型")
	private String fundsType;
	
	@TableField("product_state_id")
	@Schema(description = "产品状态ID")
	private Integer productStateId;
	
	@TableField("product_state")
	@Schema(description = "产品状态")
	private String productState;
	
	@TableField("initial_monery")
	@Schema(description = "起投金额")
	private Double initialMonery;
	
	@TableField("product_time")
	@Schema(description = "产品期限")
	private Integer productTime;
	
	@TableField("interest_rate")
	@Schema(description = "利率")
	private Double interestRate;
	
	@TableField("whether_current")
	@Schema(description = "是否活期")
	private Integer whetherCurrent;

	@TableField("delete_state")
	@Schema(description = "删除状态")
	private Integer deleteState;
	
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
