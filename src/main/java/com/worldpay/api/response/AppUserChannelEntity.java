package com.worldpay.api.response;
import java.util.Date;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class AppUserChannelEntity extends PageQueryHelperEntity{
	
	@Schema(description = "")
private Integer id;
	
	@Schema(description = "")
private String uid;
	
	@Schema(description = "")
private String userPassword;
	
	@Schema(description = "")
private String payPassword;
	
	@Schema(description = "")
private String userEmail;
	
	@Schema(description = "")
private String mobileNumber;
	
	@Schema(description = "")
private String mobilePrefix;
	
	@Schema(description = "")
private Boolean walletState;
	
	@Schema(description = "")
private String googleSecretkey;
	
	@Schema(description = "")
private Integer userState;
	
	@Schema(description = "")
private String invitationCode;
	
	@Schema(description = "")
private Double walletBalance;
	
	@Schema(description = "")
private Double freezeBalance;
	
	@Schema(description = "")
private Double openFreezeBalance;
	
	@Schema(description = "")
private String registrationId;

	@Schema(description = "")
private String tronUsdtAcount;
	
	@Schema(description = "")
private Date setTime;
	
	@Schema(description = "")
private Date gmtModified;
	
	@Schema(description = "")
private Integer kycState;
	
	@Schema(description = "")
private String kycStateName;
	
	@Schema(description = "")
private String kycAuditResult;
	
	@Schema(description = "")
private String cid;
	
	@Schema(description = "")
private String emailCode;
	
	@Schema(description = "")
private String googleCode;
	
	@Schema(description = "")
private boolean googleState;
	
	@Schema(description = "")
private String deviceName;

	@Schema(description = "")
private boolean payPasswordState;
	
	@Schema(description = "")
private String toInvitationCode;
	
	@Schema(description = "")
private Integer invitationLable;

	@Schema(description = "")
private String superName;
	

}
