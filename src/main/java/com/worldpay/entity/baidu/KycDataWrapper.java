package com.worldpay.entity.baidu;

import com.worldpay.api.request.KycApplyEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "KYC数据包装")
public class KycDataWrapper {

	 @Schema(description = "KYC申请数据")
	 private final KycApplyEntity kycData;
     
     public KycDataWrapper(KycApplyEntity kycData) {
         this.kycData = kycData;
     }
}
