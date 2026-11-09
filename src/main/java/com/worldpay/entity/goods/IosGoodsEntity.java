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
@Schema(description = "iOS商品")
public class IosGoodsEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("goods_name")
	@Schema(description = "商品名称")
	private String goodsName;
	
	@TableField("goods_num")
	@Schema(description = "商品编号")
	private String goodsNum;
	
	@TableField("goods_info")
	@Schema(description = "商品信息")
	private String goodsInfo;
	
	@TableField("goods_price")
	@Schema(description = "商品价格")
	private String goodsPrice;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
