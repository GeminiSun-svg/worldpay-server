package com.worldpay.query;
import jakarta.validation.constraints.NotBlank;

import com.worldpay.entity.appuser.AppUserHolderEntity;
import com.worldpay.entity.card.CardApplyKycEntity;
import com.worldpay.entity.card.CardApplySendEntity;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "")
public class CardApplyQueryEntity {
	
	@NotBlank(message = "用户uid不能为空")
	@Schema(description = "")
private String uid;
	
	@NotBlank(message = "银行卡id不能为空")
	@Schema(description = "")
private Integer cardId;
	
	@NotBlank(message = "银行卡uuid不能为空")
	@Schema(description = "")
private String cardUuid;
	
	@Schema(description = "")
private Integer holderId;
	
	@Schema(description = "")
private Integer topupType;
	
	@Schema(description = "")
private String payPassword;
	
	@Schema(description = "")
private CardApplySendEntity mailingAddress;
	
	@Schema(description = "")
private CardApplyKycEntity kycData;
	
	@Schema(description = "")
private String mailingAddressStr;
	
	@Schema(description = "")
private String kycDataStr;
	
	@Schema(description = "")
private AppUserHolderEntity appUserHolderData;
	
	@Schema(description = "")
private String appUserHolderStr;
	
	@Schema(description = "")
private Integer invitationLable;

}
