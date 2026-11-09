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
@Schema(description = "理财产品订单")
public class ProductOrderEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("product_id")
	@Schema(description = "产品ID")
	private Integer productId;
	
	@TableField("order_num")
	@Schema(description = "订单号")
	private String orderNum;
	
	@TableField("order_state")
	@Schema(description = "订单状态")
	private String orderState;
	
	@TableField("order_monery")
	@Schema(description = "订单金额")
	private Double orderMonery;
	
	@TableField("pay_num")
	@Schema(description = "购买份数")
	private Integer payNum;
	
	@TableField("product_name")
	@Schema(description = "产品名称")
	private String productName;
	
	@TableField("product_img")
	@Schema(description = "产品图片")
	private String productImg;
	
	@TableField("product_type")
	@Schema(description = "产品类型")
	private String productType;
	
	@TableField("funds_type")
	@Schema(description = "资金类型")
	private String fundsType;
	
	@TableField("product_time")
	@Schema(description = "产品期限")
	private Integer productTime;
	
	@TableField("interest_rate")
	@Schema(description = "利率")
	private Double interestRate;
	
	@TableField("whether_current")
	@Schema(description = "是否活期")
	private Integer whetherCurrent;

	@TableField("estimate_income")
	@Schema(description = "预计收益")
	private Double estimateIncome;
	
	@TableField("actual_income")
	@Schema(description = "实际收益")
	private Double actualIncome;
	
	@TableField("pay_time")
	@Schema(description = "支付时间")
	private Date payTime;
	
	@TableField("expire_time")
	@Schema(description = "到期时间")
	private Date expireTime;
	
	@TableField("finish_time")
	@Schema(description = "完成时间")
	private Date finishTime;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "用户信息")
	private AppUserEntity appUser;
	
	@TableField(exist = false)
	@Schema(description = "产品信息")
	private ProductEntity product;

}
