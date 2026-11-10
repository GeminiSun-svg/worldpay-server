package com.worldpay.service.impl;

import com.worldpay.utils.api.UCardApi;
import com.worldpay.utils.api.AesAPIUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.CardInfoEntity;
import com.worldpay.api.request.CardPinReqEntity;
import com.worldpay.api.request.CloseCardApiEntity;
import com.worldpay.api.request.PublicEntity;
import com.worldpay.api.request.SetPinEntity;
import com.worldpay.api.request.UnfreezeCardReqEntity;
import com.worldpay.api.response.TrackingInfoEntity;
import com.worldpay.api.response.TrackingInfoEntity.AcceptedItem;
import com.worldpay.api.response.TrackingInfoEntity.Event;
import com.worldpay.api.response.TrackingInfoEntity.Provider;
import com.worldpay.api.response.TrackingInfoEntity.Tracking;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppUserCardDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIosWalletStateDao;
import com.worldpay.dao.card.AppUserHolderDao;
import com.worldpay.dao.card.CardApplyDao;
import com.worldpay.dao.card.CardApplyManDao;
import com.worldpay.dao.card.CardCloseDao;
import com.worldpay.dao.card.CardDao;
import com.worldpay.dao.card.CardTransactionDao;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserHolderEntity;
import com.worldpay.entity.appuser.AppUserIosWalletStateEntity;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.entity.card.CardApplyManEntity;
import com.worldpay.entity.card.CardCloseEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.card.CardTransactionEntity;
import com.worldpay.entity.card.TrackingEntity;
import com.worldpay.enums.CardStateEnums;
import com.worldpay.enums.EmsStateEnums;
import com.worldpay.enums.EmsUrlEnums;
import com.worldpay.enums.LogisticsStateEnums;
import com.worldpay.enums.LogisticsSubStateEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OpenStateEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.ReviewStatusEnums;
import com.worldpay.enums.TransTypeEnums;
import com.worldpay.query.UserCardTagEntity;
import com.worldpay.service.AppUserCardService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.HttpUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.OrderCodeFactory;
import com.worldpay.utils.RedisUtil;

@Service
@Transactional
public class AppUserCardServiceImpl extends BaseApiService implements AppUserCardService{
	
	@Value("${EMS_API_KEY:}")
	private String EMS_API_KEY;
	
	@Value("${bank.aesKey:}")
	private String APPS_ECRET;
	
	@Autowired
	private AppUserCardDao appUserCardDao;
	
	@Autowired
	private CardDao cardDao;
	
	@Autowired
	private CardTransactionDao cardTransactionDao;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private AppUserHolderDao appUserHolderDao;
	
	@Autowired
	private CardApplyDao cardApplyDao;
	
	@Autowired
	private CardApplyManDao cardApplyManDao;
	
	@Autowired
	private CardCloseDao cardCloseDao;
	
	@Autowired
	private AppUserIosWalletStateDao appUserIosWalletStateDao;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	
	@Autowired
	private AppUserServiceImpl appUserServiceImpl;
	
	@Autowired
	private RedisUtil redisUtil;

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "get", remark = "查询用户总资产")
	public ResponseBase findUserCardAssets(String uid) {
		try {
			 Double balance = appUserCardDao.findUserCardAssets(uid);
		     String temp = String.format("%.2f", balance);
		     return setResultSuccess(temp, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "post", remark = "查询用户持有银行卡列表")
	public ResponseBase findUserCardList(AppUserCardEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<AppUserCardEntity> list = appUserCardDao.findStateList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
					list.get(i).setCardData(cardEntity);
				}
			}
			PageInfo<AppUserCardEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	public ResponseBase findUserCardActivationList(AppUserCardEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			if(entity.getStatus() == null) {
				entity.setStatus(CardStateEnums.ACTIVE.getIndex().toString());				
			}
			List<AppUserCardEntity> list = appUserCardDao.findPcList(entity);
//			List<AppUserCardEntity> list = appUserCardDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
					list.get(i).setCardData(cardEntity);
				}
			}
			
			PageInfo<AppUserCardEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "post", remark = "查询用户银行卡交易列表")
	public ResponseBase findTransaction(CardTransactionEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<CardTransactionEntity> list = new ArrayList<>();
			if(entity.getTransType() != null && TransTypeEnums.AUTH.getName().equals(entity.getTransType())) {
				list = cardTransactionDao.findPcList(entity);
			}else {
				list = cardTransactionDao.findList(entity);
			}
			if(list != null && list.size() > 0 ) {
				for (int i = 0; i < list.size(); i++) {
					String localCurrencyAmt = list.get(i).getLocalCurrencyAmt();
					if (localCurrencyAmt == null || localCurrencyAmt.trim().isEmpty()) {
						localCurrencyAmt = "0";
					}
					Double transMoney = Math.abs(Double.parseDouble(localCurrencyAmt));
					Double feesMoney = list.get(i).getHandlingFees() == null ? 0.0 : list.get(i).getHandlingFees();
					if(TransTypeEnums.TOPUP.getName().equals(list.get(i).getTransType())) {
					    list.get(i).setTotalManey(list.get(i).getTransCurrencyAmt());
					} else {
					    BigDecimal transMoneyBD = BigDecimal.valueOf(transMoney);
					    BigDecimal feesMoneyBD = BigDecimal.valueOf(feesMoney);
					    BigDecimal total = transMoneyBD.add(feesMoneyBD);
					    // 四舍五入到2位小数
//					    total = total.setScale(2, RoundingMode.HALF_UP);
					    list.get(i).setTotalManey("-" + total.doubleValue());
					}
					//设置多语言标题
					list.get(i).setTitle(TransTypeEnums.getLanguagName(list.get(i).getTransType(), LanguageContext.getLanguage(),list.get(i)));
					
					if(OrderStateEnums.PROCESSING.getName().equals(list.get(i).getOrderState())) {
						list.get(i).setOrderStateName(I18nUtil.getMessage("state_processing"));
					}else if(OrderStateEnums.FAILED.getName().equals(list.get(i).getOrderState())) {
						list.get(i).setOrderStateName(I18nUtil.getMessage("state_failed"));
					}
					AppUserEntity appUserEntity = appUserDao.findByUid(list.get(i).getUid());
					if(appUserEntity != null) {
						list.get(i).setUserEmail(appUserEntity.getUserEmail());
					}
					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
					if(cardEntity != null) {
						list.get(i).setCardData(cardEntity);
					}
				}
			}
			PageInfo<CardTransactionEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "get", remark = "查询用户银行卡cvv")
	public ResponseBase findCardCvv(PublicEntity entity) {
		try {
			AppUserCardEntity cardEntity = appUserCardDao.selectById(entity.getId());
			if(cardEntity != null) {
				CardInfoEntity apiEntity = new CardInfoEntity();
				apiEntity.setUserBankcardId(cardEntity.getUserBankcardId());
				ResponseBase resData = UCardApi.postData(cardEntity.getUid(), UCardMethods.BANKCARD_INFO, apiEntity, null);
				JSONObject jsonObject = JSONObject.parseObject(resData.getData().toString());
				String temp = jsonObject.getString("cardPanUrl");
				return setResultSuccess(temp, I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "post", remark = "查询用户银行卡到期时间")
	public ResponseBase findCardExpirationTime(PublicEntity entity) {
		try {
			AppUserCardEntity cardEntity = appUserCardDao.selectById(entity.getId());
			if(cardEntity != null) {
				CardInfoEntity apiEntity = new CardInfoEntity();
				apiEntity.setUserBankcardId(cardEntity.getUserBankcardId());
				ResponseBase resData = UCardApi.postData(cardEntity.getUid(), UCardMethods.BANKCARD_INFO, apiEntity, null);
				JSONObject jsonObject = JSONObject.parseObject(resData.getData().toString());
				String temp = jsonObject.getString("cardPanUrl");
				return setResultSuccess(temp, I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "get", remark = "查询用户持有银行卡详情")
	public ResponseBase findUserCardInfo(Integer id) {
		try {
			AppUserCardEntity entity = appUserCardDao.selectById(id);
			if(entity != null) {
				//持卡人信息
				CardApplyEntity applyEntity = cardApplyDao.selectById(entity.getApplyId());
				if(applyEntity != null) {
					AppUserHolderEntity holderEntity = appUserHolderDao.selectById(applyEntity.getHolderId());
					if(holderEntity != null) {
						entity.setHolderData(holderEntity);
					}
				}
				//银行卡信息
				CardEntity cardEntity = cardDao.findByuuId(entity.getCardUuid());
				entity.setCardData(cardEntity);
				if(entity.getLogisticsNum() != null && entity.getLogisticsNum().length() > 0) {
					//根据物流单号获取物流信息
					TrackingEntity trackingEntity = new TrackingEntity();
					trackingEntity.setNumber(entity.getLogisticsNum());
					List<TrackingEntity> list = new ArrayList<>();
					list.add(trackingEntity);
					String jsonParam = JSON.toJSONString(list);
					String res = HttpUtil.emsPost(EmsUrlEnums.ORDER_INFO.getName(), jsonParam,EMS_API_KEY);
					TrackingInfoEntity infoEntity = JSONObject.parseObject(res, TrackingInfoEntity.class);
					AcceptedItem temp = infoEntity.getData().getAccepted().get(0);
					if(temp.getTrack_info() != null) {
						//获取最新订单状态
						String state = temp.getTrack_info().getLatest_status().getStatus();
						if(EmsStateEnums.INTRANSIT.getName().equals(state)) {//运输中
							entity.setShippingState(LogisticsStateEnums.INTRANSIT.getIndex());
							entity.setShippingStateName(LogisticsStateEnums.INTRANSIT.getName());
						}else if(EmsStateEnums.OUTFORDELIVERY.getName().equals(state)) {//配送中
							entity.setShippingState(LogisticsStateEnums.OUTFORDELIVERY.getIndex());
							entity.setShippingStateName(LogisticsStateEnums.OUTFORDELIVERY.getName());
						}else if(EmsStateEnums.DELIVERED.getName().equals(state)) {//签收
							entity.setShippingState(LogisticsStateEnums.DELIVERED.getIndex());
							entity.setShippingStateName(LogisticsStateEnums.DELIVERED.getName());
						}
						Tracking tracking = temp.getTrack_info().getTracking();
						if(tracking != null) {
							List<Provider> providers = tracking.getProviders();
							if(providers != null && providers.size() > 0) {
								Provider provider = providers.get(0);
								if(provider != null) {
									List<Event> eventList = provider.getEvents();
									List<Event> newList = this.withSubState(eventList, entity);
									entity.setLogisticsInfo(newList);
								}
							}
						}
					}
					//修改物流状态
					appUserCardDao.updateById(entity);
				}
				return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 订单物流节点状态处理
	public List<Event> withSubState(List<Event> list, AppUserCardEntity entity) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getSub_status() != null && list.get(i).getSub_status().length() > 0) {
					String states = list.get(i).getSub_status();
					list.get(i).setSub_status(LogisticsSubStateEnums.getVale(states));
					list.get(i).setSub_statusId(LogisticsSubStateEnums.getLable(states));
				} else {
					list.get(i).setSub_status(entity.getShippingStateName());
					list.get(i).setSub_statusId(entity.getShippingState());
				}
			}
		}
		return list;
	}

	@Override
	@SysLogAnnotation(module = "用户银行卡", type = "post", remark = "查询用户持有银行卡列表")
	public ResponseBase pcFindUserCardList(AppUserCardEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<AppUserCardEntity> list = appUserCardDao.findPcList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
					list.get(i).setCardData(cardEntity);
					CardApplyManEntity manEntity = cardApplyManDao.findByApplyId(list.get(i).getApplyId());
					list.get(i).setManData(manEntity);
					list.get(i).setStateName(CardStateEnums.getValue(list.get(i).getStatus()));;
				}
			}
			PageInfo<AppUserCardEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	public ResponseBase closeCard(CardCloseEntity entity) {
		try {
			AppUserCardEntity userCardEntity = appUserCardDao.findUserBankcardId(entity.getUserBankcardId());
			if(userCardEntity == null) {
				return setResultError(I18nUtil.getMessage("parameter_error"));
			}
			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
			if(userEntity == null) {
				return setResultError(I18nUtil.getMessage("parameter_error"));
			}
			AppUserIosWalletStateEntity stateEntity = appUserIosWalletStateDao.findUid(entity.getUid());
			if(stateEntity == null || OpenStateEnums.OPEN.getIndex().equals(stateEntity.getWalletState())) {
				if(userEntity.getPayPassword() != null && userEntity.getPayPassword().length() > 0 ) {
					if(entity.getPayPassword() == null || entity.getPayPassword().length() < 1) {
						return setResultError(I18nUtil.getMessage("pay_password_error"));
					}
					if (!DigestUtils.md5DigestAsHex((entity.getPayPassword()).getBytes())
							.equals(userEntity.getPayPassword())) {
						return setResultError(I18nUtil.getMessage("pay_password_error"));
					}
				}else {
					return setResult(Constants.HTTP_RES_CODE_602, I18nUtil.getMessage("pay_password_error"), null);

				}
			}
			//封装参数
			CloseCardApiEntity apiEntity = new CloseCardApiEntity();
			apiEntity.setUserBankcardId(entity.getUserBankcardId());
			//调用关闭银行卡
			String requestOrderId = OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUserBankcardId().toString()));
			ResponseBase resData = UCardApi.postData(userCardEntity.getUid(), UCardMethods.CLOSE_CARD, apiEntity, requestOrderId);
			if(Constants.HTTP_RES_CODE_200.equals(resData.getRtncode())) {
				//添加关闭银行卡记录
				entity.setUid(userCardEntity.getUid());
				entity.setBalance(userCardEntity.getBalance());
				entity.setCardId(userCardEntity.getCardId());
				entity.setCardNo(userCardEntity.getCardNo());
				entity.setCardType(userCardEntity.getCardType());
				entity.setCardUuid(userCardEntity.getCardUuid());
				entity.setRequestOrderId(requestOrderId);
				entity.setReviewStatus(ReviewStatusEnums.PROCESSING.getIndex());
				GenericityUtil.setDate(entity);
				cardCloseDao.insert(entity);
				//修改银行卡状态
				userCardEntity.setStatus(CardStateEnums.BEFORE_CANCELLATION.getIndex().toString());
				appUserCardDao.updateById(userCardEntity);
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.CARD_CLOSE_PROCESSING_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						entity.getUid(), 
						userCardEntity.getCardNo());
				
				double balance = userCardEntity.getBalance() == null ? 0 : userCardEntity.getBalance();
				if(balance > 0) {
					return setResultSuccess(I18nUtil.getMessage("close_card_msg_money"));
				}
				return setResultSuccess(I18nUtil.getMessage("close_card_msg"));
			}
			return resData;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	

	@Override
	public ResponseBase upTag(UserCardTagEntity entity) {
		try {
			AppUserCardEntity userCardEntity = appUserCardDao.findUserBankcardId(entity.getUserBankcardId());
			if(userCardEntity != null) {
				userCardEntity.setTag(entity.getTag());
				appUserCardDao.updateById(userCardEntity);
				return setResultSuccess(userCardEntity, I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_data_null"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase setPin(CardPinReqEntity entity) {
		try {
			//支付密码校验
			ResponseBase passwordBase = appUserServiceImpl.checkPayPasswrod(entity.getUid(), entity.getPayPassword());
			if(!Constants.HTTP_RES_CODE_200.equals(passwordBase.getRtncode())) {
				return passwordBase;
			}
			AppUserCardEntity userCard= appUserCardDao.findUserBankcardId(entity.getUserBankcardId());
			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
			if(userCard != null && userEntity != null) {
				if(redisUtil.get(userEntity.getUserEmail()) ==null) {
					return setResultError(I18nUtil.getMessage("verification_error"));
				}
				String code = redisUtil.get(userEntity.getUserEmail()).toString();
				if(code == null || code.isEmpty()) {
					return setResultError(I18nUtil.getMessage("verification_error"));
				}
				if(!code.equals(entity.getEmailCode())) {
					return setResultError(I18nUtil.getMessage("verification_error"));
				}

				SetPinEntity pinEntity = new SetPinEntity();
				String aesPin = AesAPIUtils.encrypt(entity.getPin(), APPS_ECRET);
				pinEntity.setPin(aesPin);
				pinEntity.setUserBankcardId(entity.getUserBankcardId());
				ResponseBase base = UCardApi.postData(entity.getUid(), UCardMethods.SET_BANKCARD_PIN, pinEntity, null);
				if(Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
					userCard.setPinNum(aesPin);
					appUserCardDao.updateById(userCard);
					//消息处理
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.CARD_RESET_PIN_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							userEntity.getUid(), userCard.getCardNo());
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}
				return base;
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void sendSetPinMsg(AppUserCardEntity userCard, AppUserEntity userEntity) {
		//发送通知
		messageTemplateServiceImpl.unifiedSendAsync(
				MessageEnums.CARD_RESET_PIN_ZH.getIndex(), 
				LanguageContext.getLanguage(),
				userCard.getUid(), 
				userCard.getCardNo());

	}

//	@Override
//	public ResponseBase findUserCardListV2(AppUserCardEntity entity) {
//		try {
//			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
//			List<AppUserCardEntity> list = appUserCardDao.findStateList(entity);
//			if(list != null && list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
//					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
//					list.get(i).setCardData(cardEntity);
//				}
//			}
//			PageInfo<AppUserCardEntity> info = new PageInfo<>(list);
//			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//	}
//
//	@Override
//	public ResponseBase findUserCardActivationListV2(AppUserCardEntity entity) {
//		try {
//			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
//			if(entity.getStatus() == null) {
//				entity.setStatus(CardStateEnums.ACTIVE.getIndex().toString());				
//			}
//			List<AppUserCardEntity> list = appUserCardDao.findPcList(entity);
//			if(list != null && list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
//					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
//					list.get(i).setCardData(cardEntity);
//				}
//			}
//			
//			PageInfo<AppUserCardEntity> info = new PageInfo<>(list);
//			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//	}

	@Override
	public ResponseBase unfreeze(Integer id) {
		try {
			AppUserCardEntity cardEntity = appUserCardDao.selectById(id);
			if(cardEntity != null) {
				//调用接口解冻
				UnfreezeCardReqEntity apiEntity = new UnfreezeCardReqEntity();
				apiEntity.setUserBankcardId(cardEntity.getUserBankcardId());
				apiEntity.setEnable(true);
				ResponseBase apiBase = UCardApi.postData(cardEntity.getUid(), UCardMethods.UPDATE_CARD_STATUS, apiEntity, null);
				if(Constants.HTTP_RES_CODE_200.equals(apiBase.getRtncode())) {
					cardEntity.setStatus(CardStateEnums.ACTIVE.getIndex().toString());
					appUserCardDao.updateById(cardEntity);
				}
				return apiBase;
			}
			return setResultError(I18nUtil.getMessage("base_data_null"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
}
