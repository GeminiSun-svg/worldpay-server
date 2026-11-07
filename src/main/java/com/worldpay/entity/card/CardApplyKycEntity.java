package com.worldpay.entity.card;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_apply_kyc")
@Schema(description = "")
public class CardApplyKycEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("apply_id")
	@Schema(description = "")
private Integer applyId;
	
	@TableField("paperwork_type")
	@Schema(description = "")
private String paperworkType;
	
	@TableField("paperwork_num")
	@Schema(description = "")
private String paperworkNum;
	
	@TableField("expiration_time")
	@Schema(description = "")
private String expirationTime;
	
	@TableField("front_photo_id")
	@Schema(description = "")
private String frontPhotoId;
	
	@TableField("front_photo_url")
	@Schema(description = "")
private String frontPhotoUrl;
	
	@TableField("back_photo_id")
	@Schema(description = "")
private String backPhotoId;
	
	@TableField("back_photo_url")
	@Schema(description = "")
private String backPhotoUrl;
	
	@TableField("handheld_photo_id")
	@Schema(description = "")
private String handheldPhotoId;
	
	@TableField("handheld_photo_url")
	@Schema(description = "")
private String handheldPhotoUrl;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
