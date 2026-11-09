package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category kyc申请
 * @author Hlin
 *
 */
@Data
@Schema(description = "KYC申请请求")
public class KycApplyEntity {
	
	@Schema(description = "证件上的名字")
	private String firstName;
	
	@Schema(description = "证件上的姓氏")
	private String lastName;
	
	@Schema(description = "身份证件号码")
	private String idNo;
	
	@Schema(description = "用户邮箱地址")
	private String email;
	
	@Schema(description = "用户国籍代码")
	private String nationCode;
	
	@Schema(description = "身份证件类型")
	private String certType;
	
	@Schema(description = "证件正面照URL")
	private String idUrl;
	
	@Schema(description = "证件背面照URL")
	private String idBackUrl;
	
	@Schema(description = "用户出生日期")
	private String birthday;
	
	@Schema(description = "国家地区代码")
	private String countryCode;
	
	@Schema(description = "手机号码区号")
	private String areaCode;
	
	@Schema(description = "用户手机号码")
	private String phone;
	
	@Schema(description = "上传文件类型")
	private Integer fileType;
	
	@Schema(description = "上传文件URL")
	private String fileUrl;
	
	@Schema(description = "人脸照片URL")
	private String faceUrl;

}
