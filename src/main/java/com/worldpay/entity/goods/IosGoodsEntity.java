package com.worldpay.entity.goods;
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
@TableName("ios_goods")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class IosGoodsEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("goods_name")
	@Schema(description = "")
private String goodsName;
	
	@TableField("goods_num")
	@Schema(description = "")
private String goodsNum;
	
	@TableField("goods_info")
	@Schema(description = "")
private String goodsInfo;
	
	@TableField("goods_price")
	@Schema(description = "")
private String goodsPrice;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
