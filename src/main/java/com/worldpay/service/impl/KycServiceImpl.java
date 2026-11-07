package com.worldpay.service.impl;

import com.worldpay.utils.api.UCardApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.KycApplyEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.card.CardApplyDao;
import com.worldpay.dao.card.CardApplyKycDao;
import com.worldpay.dao.card.CardApplyManDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.entity.card.CardApplyKycEntity;
import com.worldpay.entity.card.CardApplyManEntity;
import com.worldpay.enums.KycStateEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.service.KycService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class KycServiceImpl extends BaseApiService implements KycService{
	
	@Autowired
	private CardApplyDao applyDao;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private CardApplyManDao cardApplyManDao;
	
	@Autowired
	private CardApplyKycDao kycDao;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	

	@Override
	@SysLogAnnotation(module = "用户Kyc认证", type = "get", remark = "申请kyc")
	public ResponseBase apply(Integer applyId) {
		try {
			CardApplyEntity applyEntity = applyDao.selectById(applyId);
			if(applyEntity != null) {
				//查询相关数据
				AppUserEntity userEntity = appUserDao.findByUid(applyEntity.getUid());
				if(userEntity == null) {
					return setResultError(I18nUtil.getMessage("account_error"));
				}
				CardApplyManEntity manEntity = cardApplyManDao.findByApplyId(applyId);
				if(manEntity == null) {
					return setResultError(I18nUtil.getMessage("holder_null"));
				}
//				CardApplySendEntity sendEntity = sendDao.findByApplyId(applyId);
//				if(sendEntity == null) {
//					return setResultError(I18nUtil.getMessage("receiving_info_null"));
//				}
				CardApplyKycEntity kycEntity = kycDao.findByApplyId(applyId);
				if(kycEntity == null) {
					return setResultError(I18nUtil.getMessage("kyc_info_null"));
				}
				//构建kyc申请参数
				KycApplyEntity kycData = new KycApplyEntity();
				kycData.setAreaCode(manEntity.getUserTelDialCode());
				kycData.setBirthday(manEntity.getUserBirthday());
				kycData.setCertType(kycEntity.getPaperworkType());
				kycData.setCountryCode(manEntity.getUserTelCode());
				kycData.setEmail(userEntity.getUserEmail());
				kycData.setIdBackUrl(kycEntity.getBackPhotoUrl());
				kycData.setIdNo(kycEntity.getPaperworkNum());
				kycData.setIdUrl(kycEntity.getFrontPhotoUrl());
				kycData.setLastName(manEntity.getUserSurname());
				kycData.setFirstName(manEntity.getUserName());
				kycData.setNationCode(manEntity.getUserTelCode());
				kycData.setPhone(manEntity.getUserTel());
				ResponseBase resData = UCardApi.postData(userEntity.getUid(), UCardMethods.USER_KYC_APPLY, kycData, null);
				if(resData.getRtncode().equals(Constants.HTTP_RES_CODE_200)) {//发送kyc审核提交通知
					applyEntity.setKycState(KycStateEnums.PROCESS_APPROVE.getIndex());
					applyEntity.setKycStateName(KycStateEnums.PROCESS_APPROVE.getName());
					applyDao.updateById(applyEntity);
					userEntity.setKycState(KycStateEnums.PROCESS_APPROVE.getIndex());
					userEntity.setKycStateName(KycStateEnums.PROCESS_APPROVE.getName());
					appUserDao.updateById(userEntity);
					//消息处理
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.KYC_SUBMIT_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							userEntity.getUid());
					
					return resData;
				}else {//发送kyc审核不通过通知
					//消息处理
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							userEntity.getUid(),
							resData.getMsg());
					return resData;
				}
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	
}
