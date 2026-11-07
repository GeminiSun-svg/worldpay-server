package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category kyc申请
 * @author Hlin
 *
 */
@Data
public class KycApplyEntity {
	
	@Schema(description = "")
private String firstName;
	
	@Schema(description = "")
private String lastName;
	
	@Schema(description = "")
private String idNo;
	
	@Schema(description = "")
private String email;
	
	@Schema(description = "")
private String nationCode;
	
	@Schema(description = "")
private String certType;
	
	@Schema(description = "")
private String idUrl;
	
	@Schema(description = "")
private String idBackUrl;
	
	@Schema(description = "")
private String birthday;
	
	@Schema(description = "")
private String countryCode;
	
	@Schema(description = "")
private String areaCode;
	
	@Schema(description = "")
private String phone;
	
	@Schema(description = "")
private Integer fileType;
	
	@Schema(description = "")
private String fileUrl;
	
	@Schema(description = "")
private String faceUrl;

}
