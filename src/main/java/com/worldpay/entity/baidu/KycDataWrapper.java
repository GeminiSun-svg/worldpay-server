package com.worldpay.entity.baidu;

import com.worldpay.api.request.KycApplyEntity;

import lombok.Data;

@Data
public class KycDataWrapper {

	 private final KycApplyEntity kycData;
     
     public KycDataWrapper(KycApplyEntity kycData) {
         this.kycData = kycData;
     }
}
