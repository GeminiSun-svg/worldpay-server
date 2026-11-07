package com.worldpay.entity.appuser;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("app_user_file")
@Schema(description = "")
public class AppUserFileEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("cert_type")
	@Schema(description = "")
private Integer certType;
	
	@TableField("document_file_url")
	@Schema(description = "")
private String documentFileUrl;
	
	@TableField("document_file_id")
	@Schema(description = "")
private String documentFileId;
	
	@TableField("document_type")
	@Schema(description = "")
private Integer documentType;
	
	@TableField("document_type_name")
	@Schema(description = "")
private String documentTypeName;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private String frontPhotoId;
	
	@TableField(exist = false)
	@Schema(description = "")
private String frontPhotoUrl;
	
	@TableField(exist = false)
	@Schema(description = "")
private String backPhotoId;
	
	@TableField(exist = false)
	@Schema(description = "")
private String backPhotoUrl;
	
	@TableField(exist = false)
	@Schema(description = "")
private String handheldPhotoId;
	
	@TableField(exist = false)
	@Schema(description = "")
private String handheldPhotoUrl;

}
