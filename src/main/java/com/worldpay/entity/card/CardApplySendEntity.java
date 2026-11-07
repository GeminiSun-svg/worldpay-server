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
@Schema(description = "")
public class CardApplySendEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("apply_id")
	@Schema(description = "")
private Integer applyId;
	
	@TableField("nation")
	@Schema(description = "")
private String nation;
	
	@TableField("province")
	@Schema(description = "")
private String province;
	
	@TableField("city")
	@Schema(description = "")
private String city;
	
	@TableField("address_info")
	@Schema(description = "")
private String addressInfo;
	
	@TableField("collect_man")
	@Schema(description = "")
private String collectMan;
	
	@TableField("collect_tel")
	@Schema(description = "")
private String collectTel;
	
	@TableField("post_code")
	@Schema(description = "")
private String postCode;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
