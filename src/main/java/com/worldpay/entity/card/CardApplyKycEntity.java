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
@Schema(description = "卡片申请实名认证信息")
public class CardApplyKycEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("apply_id")
	@Schema(description = "申请ID")
	private Integer applyId;
	
	@TableField("paperwork_type")
	@Schema(description = "证件类型")
	private String paperworkType;
	
	@TableField("paperwork_num")
	@Schema(description = "证件号码")
	private String paperworkNum;
	
	@TableField("expiration_time")
	@Schema(description = "证件有效期")
	private String expirationTime;
	
	@TableField("front_photo_id")
	@Schema(description = "证件正面照片ID")
	private String frontPhotoId;
	
	@TableField("front_photo_url")
	@Schema(description = "证件正面照片地址")
	private String frontPhotoUrl;
	
	@TableField("back_photo_id")
	@Schema(description = "证件反面照片ID")
	private String backPhotoId;
	
	@TableField("back_photo_url")
	@Schema(description = "证件反面照片地址")
	private String backPhotoUrl;
	
	@TableField("handheld_photo_id")
	@Schema(description = "手持证件照片ID")
	private String handheldPhotoId;
	
	@TableField("handheld_photo_url")
	@Schema(description = "手持证件照片地址")
	private String handheldPhotoUrl;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
