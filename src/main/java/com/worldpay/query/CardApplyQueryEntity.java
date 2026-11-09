package com.worldpay.query;
import jakarta.validation.constraints.NotBlank;

import com.worldpay.entity.appuser.AppUserHolderEntity;
import com.worldpay.entity.card.CardApplyKycEntity;
import com.worldpay.entity.card.CardApplySendEntity;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "开卡申请查询参数")
public class CardApplyQueryEntity {
	
	@NotBlank(message = "用户uid不能为空")
	@Schema(description = "用户ID")
	private String uid;
	
	@NotBlank(message = "银行卡id不能为空")
	@Schema(description = "卡片ID")
	private Integer cardId;
	
	@NotBlank(message = "银行卡uuid不能为空")
	@Schema(description = "卡片唯一标识")
	private String cardUuid;
	
	@Schema(description = "持卡人ID")
	private Integer holderId;
	
	@Schema(description = "首充支付方式")
	private Integer topupType;
	
	@Schema(description = "支付密码")
	private String payPassword;
	
	@Schema(description = "收件地址信息")
	private CardApplySendEntity mailingAddress;
	
	@Schema(description = "KYC资料信息")
	private CardApplyKycEntity kycData;
	
	@Schema(description = "收件地址JSON字符串")
	private String mailingAddressStr;
	
	@Schema(description = "KYC资料JSON字符串")
	private String kycDataStr;
	
	@Schema(description = "持卡人信息")
	private AppUserHolderEntity appUserHolderData;
	
	@Schema(description = "持卡人信息JSON字符串")
	private String appUserHolderStr;
	
	@Schema(description = "邀请人类型标签")
	private Integer invitationLable;

}
