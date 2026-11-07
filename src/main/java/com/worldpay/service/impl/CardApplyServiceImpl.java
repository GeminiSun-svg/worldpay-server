package com.worldpay.service.impl;

import com.worldpay.utils.api.UCardApi;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.OpenCardEntity;
import com.worldpay.api.request.RechargeCardEntity;
import com.worldpay.api.response.TrackingInfoEntity.Event;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppUserCardDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.appuser.AppUserIosWalletStateDao;
import com.worldpay.dao.card.CardApplyDao;
import com.worldpay.dao.card.CardApplyKycDao;
import com.worldpay.dao.card.CardApplyManDao;
import com.worldpay.dao.card.CardApplySendDao;
import com.worldpay.dao.card.CardDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.appuser.AppUserIosWalletStateEntity;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.entity.card.CardApplyKycEntity;
import com.worldpay.entity.card.CardApplyManEntity;
import com.worldpay.entity.card.CardApplySendEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.CardStateEnums;
import com.worldpay.enums.CardTypeEnums;
import com.worldpay.enums.KycApiStateEnums;
import com.worldpay.enums.LogisticsSubStateEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OpenCardStateEnums;
import com.worldpay.enums.OpenStateEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.query.RejectApplyEntity;
import com.worldpay.service.CardApplyService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.OrderCodeFactory;

import lombok.Synchronized;

@Service
@Transactional
public class CardApplyServiceImpl extends BaseApiService implements CardApplyService{
	
	@Autowired
	private CardApplyDao cardApplyDao;
	
	@Autowired
	private CardApplyManDao cardApplyManDao;
	
	@Autowired
	private CardApplySendDao cardApplySendDao;
	
	@Autowired
	private CardDao cardDao;
	
	@Autowired
	private AppUserCardDao appUserCardDao;
	
	@Autowired
	private CardApplyKycDao applyKycDao;
	
	@Autowired
	@Lazy
	private CardServiceImpl cardServiceImpl;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private WalletLogDao walletLogDao;
	
	@Autowired
	private AppUserIosWalletStateDao appUserIosWalletStateDao;
	
	@Autowired
	private AppUserIntiterDao appUserIntiterDao;
	
	@Autowired
	private ChannelUserDao channelUserDao;
	
    @Value("${invitation.default_code:}")
    private String INVITATION_DEFAULT_CODE;
	
	@Value("${EMS_API_KEY:}")
	private String EMS_API_KEY;

	@Override
	@SysLogAnnotation(module = "银行卡申请记录", type = "post", remark = "查询申请记录")
	public ResponseBase openCardApply(CardApplyEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<CardApplyEntity> list = cardApplyDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					CardApplyManEntity applyManEntity = cardApplyManDao.findByApplyId(list.get(i).getId());
					list.get(i).setApplyManData(applyManEntity);
					CardApplySendEntity applySendEntity = cardApplySendDao.findByApplyId(list.get(i).getId());
					list.get(i).setApplySendData(applySendEntity);
					CardEntity cardEntity = cardDao.findByuuId(list.get(i).getCardUuid());
					list.get(i).setCardData(cardEntity);
					CardApplyKycEntity applyKycEntity = applyKycDao.findByApplyId(list.get(i).getId());
					list.get(i).setKycData(applyKycEntity);
					AppUserCardEntity userCardEntity = appUserCardDao.findApplyId(list.get(i).getId());
					list.get(i).setUserCardData(userCardEntity);
				}
			}
			PageInfo<CardApplyEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡申请记录", type = "get", remark = "申请记录详情")
	public ResponseBase openCardApplyInfo(Integer id) {
		try {
			CardApplyEntity entity = cardApplyDao.selectById(id);
			if(entity != null) {
				CardApplyManEntity applyManEntity = cardApplyManDao.findByApplyId(id);
				entity.setApplyManData(applyManEntity);
				CardApplySendEntity applySendEntity = cardApplySendDao.findByApplyId(id);
				entity.setApplySendData(applySendEntity);
				CardEntity cardEntity = cardDao.findByuuId(entity.getCardUuid());
				entity.setCardData(cardEntity);
				CardApplyKycEntity applyKycEntity = applyKycDao.findByApplyId(id);
				entity.setKycData(applyKycEntity);
				AppUserCardEntity userCard = appUserCardDao.findApplyId(id);
				if(userCard != null) {
					entity.setUserCardData(userCard);
				}
			}
			return setResultSuccess(entity, Constants.SUCCESS);
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
	@Synchronized
	@SysLogAnnotation(module = "银行卡申请记录", type = "get", remark = "开卡首次充值激活")
	public ResponseBase openCard(Integer id) {
		try {
			CardApplyEntity entity = cardApplyDao.selectById(id);
			if(entity != null && CardTypeEnums.VIRTUAL.getName().equals(entity.getCardType())) {//虚拟卡申请
				if(!OpenCardStateEnums.WAIT_ACTIVATION.getIndex().equals(entity.getApplyState())) {
					return setResultError(I18nUtil.getMessage("card_open_state"));
				}
				if(!KycApiStateEnums.SUCCESS.getIndex().equals(entity.getKycState())) {
					return setResultError(I18nUtil.getMessage("user_kyc_state"));
				}
				//查询开卡银行卡
				CardEntity cardEntity = cardDao.findByuuId(entity.getCardUuid());
				if(cardEntity == null) {
					return setResultError(I18nUtil.getMessage("bank_card_null"));
				}
				Double minLimi = cardEntity.getActiveMinLimit() == null ? 0 : cardEntity.getActiveMinLimit();
				if(minLimi > entity.getPreSaveCost()) {
					return setResultError(I18nUtil.getMessage("topup_money_min"));
				}
				OpenCardEntity apiEntity = new OpenCardEntity();
				apiEntity.setProductId(entity.getCardId());
				//调用三方开卡
				ResponseBase resData = UCardApi.postData(entity.getUid(), UCardMethods.APPLY_BANKCARD, apiEntity, null);
				if (resData.getRtncode() == Constants.HTTP_RES_CODE_200) {
					//保存用户银行卡信息
					String apiData = resData.getData().toString();
					AppUserCardEntity appUserCardEntity = JSONObject.parseObject(apiData, AppUserCardEntity.class);
					appUserCardEntity.setCardId(entity.getCardId());
					appUserCardEntity.setCardUuid(entity.getCardUuid());
					appUserCardEntity.setCardType(entity.getCardType());
					appUserCardEntity.setUid(entity.getUid());
					appUserCardEntity.setApplyId(entity.getId());
					appUserCardEntity.setStatus(CardStateEnums.ACTIVE_PROCESSING.getIndex().toString());
					GenericityUtil.setDate(appUserCardEntity);
					appUserCardDao.insert(appUserCardEntity);
					Thread.sleep(3000);
					//调用三方充值
					RechargeCardEntity rechargeCard = new RechargeCardEntity();
					rechargeCard.setAmount(entity.getPreSaveCost());
					rechargeCard.setHandlingFees(entity.getOpenCardCost());
					rechargeCard.setPayType(entity.getTopupType());
					rechargeCard.setTargetAmount(entity.getPreSaveCost());
					rechargeCard.setUid(entity.getUid());
					rechargeCard.setUserBankcardId(appUserCardEntity.getUserBankcardId());
					rechargeCard.setType(false);
					ResponseBase base = cardServiceImpl.firstTopUp(rechargeCard,entity);
					//修改申请记录状态
					entity.setApplyState(OpenCardStateEnums.PROCESS_ACTIVATION.getIndex());
					entity.setApplyStateName(OpenCardStateEnums.PROCESS_ACTIVATION.getName());
					cardApplyDao.updateById(entity);
					if(Constants.HTTP_RES_CODE_500.equals(base.getRtncode()) 
							&& "Activation processing in progress, We will notify you of the processing results later.".equals(base.getMsg())) {
						return setResultSuccess(I18nUtil.getMessage("base_success"));
					}
					return base;
				}
				return resData;
			}else if(entity != null && CardTypeEnums.PHYSICAL.getName().equals(entity.getCardType())) {//实体卡申请
				
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase reject(RejectApplyEntity entity) {
		try {
			CardApplyEntity applyEntity = cardApplyDao.selectById(entity.getId());
			if(applyEntity != null) {
				applyEntity.setApplyState(OpenCardStateEnums.ERROR_ACTIVATION.getIndex());
				applyEntity.setApplyStateName(OpenCardStateEnums.ERROR_ACTIVATION.getName());
				applyEntity.setRejectInfo(entity.getRejectInfo());
				applyEntity.setRejectUser(entity.getRejectUser());
				applyEntity.setRejectUserName(entity.getRejectUserName());
				cardApplyDao.updateById(applyEntity);
				//解冻冻结金额
				AppUserEntity appUserEntity = appUserDao.findByUid(applyEntity.getUid());
				if(appUserEntity != null) {
					appUserEntity.setWalletBalance(appUserEntity.getWalletBalance() + applyEntity.getOpenCardTotal());
					appUserEntity.setOpenFreezeBalance((appUserEntity.getOpenFreezeBalance() == null ? 0: appUserEntity.getOpenFreezeBalance()) 
							- (applyEntity.getOpenCardTotal() == null ? 0: applyEntity.getOpenCardTotal()));
					appUserDao.updateById(appUserEntity);
				}
				//新增钱包解冻记录
				this.addWalletOpenFreeze(applyEntity, appUserEntity, applyEntity.getOpenCardTotal(), appUserEntity.getWalletBalance());
				
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.APPLY_CARD_ERROR_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						applyEntity.getUid(), 
						entity.getRejectInfo());
				
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public WalletLogEntity addWalletOpenFreeze(CardApplyEntity applyEntity, AppUserEntity userEntity, Double money, Double balance) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
			walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
			walletLogEntity.setTitle(WalletTitleTypeEnums.OPEN_CARD_THAW.getCnName());
			walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(money));
			walletLogEntity.setFormName(userEntity.getUserEmail());
			walletLogEntity.setFormAccount(userEntity.getUserEmail());
			walletLogEntity.setToName(userEntity.getUserEmail());
			walletLogEntity.setToAccount(userEntity.getUserEmail());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.OPEN_CARD_THAW.getIndex());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
			return walletLogEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase rejectV2(RejectApplyEntity entity) {
		try {
			CardApplyEntity applyEntity = cardApplyDao.selectById(entity.getId());
			if(applyEntity != null) {
				applyEntity.setApplyState(OpenCardStateEnums.ERROR_ACTIVATION.getIndex());
				applyEntity.setApplyStateName(OpenCardStateEnums.ERROR_ACTIVATION.getName());
				applyEntity.setRejectInfo(entity.getRejectInfo());
				applyEntity.setRejectUser(entity.getRejectUser());
				applyEntity.setRejectUserName(entity.getRejectUserName());
				cardApplyDao.updateById(applyEntity);
							
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.APPLY_CARD_ERROR_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						applyEntity.getUid(), 
						entity.getRejectInfo());
				
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase openCardV2(Integer id) {
		try {
			CardApplyEntity entity = cardApplyDao.selectById(id);
			if(entity != null && CardTypeEnums.VIRTUAL.getName().equals(entity.getCardType())) {//虚拟卡申请
				if(!OpenCardStateEnums.WAIT_ACTIVATION.getIndex().equals(entity.getApplyState())) {
					return setResultError(I18nUtil.getMessage("card_open_state"));
				}
				OpenCardEntity apiEntity = new OpenCardEntity();
				apiEntity.setProductId(entity.getCardId());
				//调用三方开卡
				ResponseBase resData = UCardApi.postData(entity.getUid(), UCardMethods.APPLY_BANKCARD, apiEntity, null);
				if (resData.getRtncode() == Constants.HTTP_RES_CODE_200) {
					//保存用户银行卡信息
					String apiData = resData.getData().toString();
					AppUserCardEntity appUserCardEntity = JSONObject.parseObject(apiData, AppUserCardEntity.class);
					appUserCardEntity.setCardId(entity.getCardId());
					appUserCardEntity.setCardUuid(entity.getCardUuid());
					appUserCardEntity.setCardType(entity.getCardType());
					appUserCardEntity.setUid(entity.getUid());
					appUserCardEntity.setApplyId(entity.getId());
					appUserCardEntity.setStatus(CardStateEnums.TBA.getIndex().toString());
					GenericityUtil.setDate(appUserCardEntity);
					appUserCardDao.insert(appUserCardEntity);
					//修改申请记录状态
					entity.setApplyState(OpenCardStateEnums.WAIT_USER_ACTIVATION.getIndex());
					entity.setApplyStateName(OpenCardStateEnums.WAIT_USER_ACTIVATION.getName());
					cardApplyDao.updateById(entity);
					
					//修改钱包、usd充值入口状态
					AppUserIosWalletStateEntity stateEntity = appUserIosWalletStateDao.findUid(entity.getUid());
					if(stateEntity != null) {
						AppUserIntiterEntity intiterEntity = appUserIntiterDao.findByUidCoverInviterUser(entity.getUid());
						if(intiterEntity != null) {
							//查询渠道商邀请码
							ChannelUserEntity channelUserEntity = channelUserDao.selectById(intiterEntity.getInviterUser());
							if(channelUserEntity != null && INVITATION_DEFAULT_CODE.equals(channelUserEntity.getInvitationCode())) {
								stateEntity.setWalletState(OpenStateEnums.CLOSURE.getIndex());
								stateEntity.setTopUpState(OpenStateEnums.CLOSURE.getIndex());
							}else {
								stateEntity.setWalletState(OpenStateEnums.OPEN.getIndex());
								stateEntity.setTopUpState(OpenStateEnums.OPEN.getIndex());
							}
							appUserIosWalletStateDao.updateById(stateEntity);
						}
					}
					
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}else {
					//修改申请记录状态
//					entity.setApplyState(OpenCardStateEnums.ERROR_ACTIVATION.getIndex());
//					entity.setApplyStateName(OpenCardStateEnums.ERROR_ACTIVATION.getName());
//					cardApplyDao.updateById(entity);
					cardApplyDao.deleteById(entity.getId());
					return resData;
				}
			}else if(entity != null && CardTypeEnums.PHYSICAL.getName().equals(entity.getCardType())) {//实体卡申请
				
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
