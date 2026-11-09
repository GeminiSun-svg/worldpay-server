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
@Schema(description = "用户KYC文件")
public class AppUserFileEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("cert_type")
	@Schema(description = "证件类型")
	private Integer certType;
	
	@TableField("document_file_url")
	@Schema(description = "证件文件地址")
	private String documentFileUrl;
	
	@TableField("document_file_id")
	@Schema(description = "证件文件ID")
	private String documentFileId;
	
	@TableField("document_type")
	@Schema(description = "文件类型")
	private Integer documentType;
	
	@TableField("document_type_name")
	@Schema(description = "文件类型名称")
	private String documentTypeName;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "正面照文件ID")
	private String frontPhotoId;
	
	@TableField(exist = false)
	@Schema(description = "正面照地址")
	private String frontPhotoUrl;
	
	@TableField(exist = false)
	@Schema(description = "反面照文件ID")
	private String backPhotoId;
	
	@TableField(exist = false)
	@Schema(description = "反面照地址")
	private String backPhotoUrl;
	
	@TableField(exist = false)
	@Schema(description = "手持照文件ID")
	private String handheldPhotoId;
	
	@TableField(exist = false)
	@Schema(description = "手持照地址")
	private String handheldPhotoUrl;

}
