package com.worldpay.entity.card;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_apply_send")
@Schema(description = "卡片邮寄地址信息")
public class CardApplySendEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("apply_id")
	@Schema(description = "申请ID")
	private Integer applyId;
	
	@TableField("nation")
	@Schema(description = "国家")
	private String nation;
	
	@TableField("province")
	@Schema(description = "省份")
	private String province;
	
	@TableField("city")
	@Schema(description = "城市")
	private String city;
	
	@TableField("address_info")
	@Schema(description = "详细地址")
	private String addressInfo;
	
	@TableField("collect_man")
	@Schema(description = "收件人")
	private String collectMan;
	
	@TableField("collect_tel")
	@Schema(description = "收件人手机号")
	private String collectTel;
	
	@TableField("post_code")
	@Schema(description = "邮政编码")
	private String postCode;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
