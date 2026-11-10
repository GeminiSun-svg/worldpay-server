package com.worldpay.service.impl;

import com.worldpay.utils.api.WalletApi;
import com.worldpay.utils.api.UCardApi;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.api.request.CardInfoEntity;
import com.worldpay.api.request.ResetAddressEntity;
import com.worldpay.api.request.UserBankcardEntity;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.api.response.CardBalanceEntity;
import com.worldpay.api.response.ChannelUserLeve;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserCardDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.brokerage.BrokerageLogDao;
import com.worldpay.dao.card.AppUserCardOpenDao;
import com.worldpay.dao.card.CardApplyDao;
import com.worldpay.dao.card.CardCloseDao;
import com.worldpay.dao.card.CardDao;
import com.worldpay.dao.card.CardTransactionDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.entity.appuser.AppUserCardOpenEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.brokerage.BrokerageLogEntity;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.entity.card.CardCloseEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.card.CardTransactionEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.CardStateEnums;
import com.worldpay.enums.CardTypeEnums;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.enums.KycStateEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OpenCardStateEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.PublicEnums;
import com.worldpay.enums.ReviewStatusEnums;
import com.worldpay.enums.RobotRoleEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.TransTypeEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.service.WebhookService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.IpUtil;
import com.worldpay.utils.OrderCodeFactory;
import com.worldpay.webhook.response.CardRechargeResult;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class WebhookServiceImpl extends BaseApiService implements WebhookService {

//	@Value("${bank.appSecret:}")
//	private String APP_SECRET;

	@Autowired
	private AppUserCardDao appUserCardDao;

	@Autowired
	private CardApplyDao cardApplyDao;

	@Autowired
	private CardTransactionDao cardTransactionDao;

	@Autowired
	private WalletLogDao walletLogDao;

	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;

	@Autowired
	private CardServiceImpl cardServiceImpl;

	@Autowired
	private AppUserDao appUserDao;

	@Autowired
	private CardCloseDao cardCloseDao;
	
	@Autowired
	private AppUserCardOpenDao appUserCardOpenDao;

	@Autowired
	private AppUserIntiterDao appUserIntiterDao;
	
	@Autowired
	private ChannelUserDao channelUserDao;
	
	@Autowired
	private CardDao cardDao;
	
	@Autowired
	private BrokerageLogDao brokerageLogDao;
	
	@Autowired
	private AppChannelIntiterDao appChannelIntiterDao;
	
    @Autowired
    private IpUtil ipUtil;

	@Override
	@Synchronized
	public ApiResponseEntity agentNotify(CardRechargeResult entity, HttpServletRequest request) {
		try {
			if (entity != null && entity.getEventType().equals("cardRechargeResult")) {// 充值
				return this.topUpCallback(entity);
			}else if (entity != null && entity.getEventType().equals("transactionCreated")) {// 交易记录
				return this.transactionCreated(entity);
			}else if (entity != null && entity.getEventType().equals("cardStatusChange")) {// 卡状态变更回调
				return this.cardStateUp(entity);
			}else if(entity != null && entity.getEventType().equals("3ds")) {//验证码回调
				return this.transactionVerificationCode(entity);
			}else if(entity != null && entity.getEventType().equals("kycStatusChange")) {//kyc认证状态回调
//				return this.kycStatusChange(entity);
			}else if(entity != null && entity.getEventType().equals("merchantRecharge")) {//商户充值回调
				return this.merchantRecharge(entity);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 卡充值回调
	public ApiResponseEntity topUpCallback(CardRechargeResult result) {
		try {
			System.out.println("********************银行卡充值回调**********************");
			if (result.getRechargeAmount() == null || Double.parseDouble(result.getRechargeAmount()) <= 0.0) {
				return ApiResponseEntity.success();
			}
			// 新三方带 status：非成功直接结束（已有预订单则标失败）
			if (result.getStatus() != null && !"SUCCESS".equalsIgnoreCase(result.getStatus())
					&& !"success".equalsIgnoreCase(result.getStatus())) {
				CardTransactionEntity failTx = cardTransactionDao.findOrderNum(result.getOrderId());
				if (failTx != null) {
					failTx.setOrderState(OrderStateEnums.FAILED.getName());
					cardTransactionDao.updateById(failTx);
				}
				log.warn("充值回调非成功 status={}, orderId={}, userBankcardId={}",
						result.getStatus(), result.getOrderId(), result.getUserBankcardId());
				return ApiResponseEntity.success();
			}
			AppUserCardEntity appUserCard = appUserCardDao.findUserBankcardId(result.getUserBankcardId());
			if (appUserCard == null) {
				return ApiResponseEntity.error();
			}
			// 实体卡/虚拟卡：卡未激活时仍允许落充值成功单并同步余额（避免仅因状态校验丢记录）
			CardTransactionEntity transaction = cardTransactionDao.findOrderNum(result.getOrderId());
			if (transaction != null) {
				transaction.setCardNo(appUserCard.getCardNo());
				transaction.setOrderState(OrderStateEnums.POSTED.getName());
				cardTransactionDao.updateById(transaction);
			} else {
				// 下单时未落库（三方返回码与本地判断不一致等）时，按回调补建 TOPUP 记录
				log.warn("充值回调未找到预订单，按回调补建 orderId={}, userBankcardId={}, amount={}",
						result.getOrderId(), result.getUserBankcardId(), result.getRechargeAmount());
				transaction = new CardTransactionEntity();
				transaction.setUid(appUserCard.getUid());
				transaction.setUserBankcardId(appUserCard.getUserBankcardId());
				transaction.setCardId(appUserCard.getCardId());
				transaction.setCardUuid(appUserCard.getCardUuid());
				transaction.setCardNo(appUserCard.getCardNo());
				transaction.setHandlingFees(0.0);
				transaction.setRequestOrderId(result.getOrderId());
				transaction.setOrderState(OrderStateEnums.POSTED.getName());
				transaction.setLocalCurrency(TransCurrencyEnums.USD.getName());
				transaction.setLocalCurrencyAmt("+" + result.getRechargeAmount());
				transaction.setTransCurrency(TransCurrencyEnums.USD.getName());
				transaction.setTransCurrencyAmt("+" + result.getRechargeAmount());
				transaction.setTransType(TransTypeEnums.TOPUP.getName());
				GenericityUtil.setDate(transaction);
				cardTransactionDao.insert(transaction);
			}
			// 修改钱包消费记录状态(佣金处理)
			this.upWalletLog(result, appUserCard, transaction);
			// 查询银行卡余额，修改数据库卡余额
			this.upUserCardBalance(appUserCard, transaction);
			return ApiResponseEntity.success();
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @category 卡充值修改钱包消费记录状态
	 * @param result
	 * @param appUserCard
	 * @param transaction 
	 */
	public void upWalletLog(CardRechargeResult result, AppUserCardEntity appUserCard, CardTransactionEntity transaction) {
		AppUserEntity userEntity = appUserDao.findByUid(appUserCard.getUid());
		//开卡记录
		WalletLogEntity openCardWalletLog = walletLogDao.findOutOrderNoOrType(result.getOrderId(),WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getIndex());
		if(openCardWalletLog != null) {
			openCardWalletLog.setToAccount(appUserCard.getCardNo());
			openCardWalletLog.setToName(appUserCard.getCardNo());
			openCardWalletLog.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogDao.updateById(openCardWalletLog);
			//开卡记录处理
			AppUserCardOpenEntity appUserCardOpenEntity = appUserCardOpenDao.findOrderNum(result.getOrderId());
			if(appUserCardOpenEntity != null) {
				appUserCardOpenEntity.setOpenState(PublicEnums.TOW.getIndex());
				appUserCardOpenDao.updateById(appUserCardOpenEntity);
			}
			//消息处理
            this.sendWalletMsg(userEntity,openCardWalletLog);
		}
		//充值记录
		WalletLogEntity walletLog = walletLogDao.findOutOrderNoOrType(result.getOrderId(),WalletTitleTypeEnums.CARD_TOP_UP.getIndex());
		if (walletLog != null) {
			walletLog.setToAccount(appUserCard.getCardNo());
			walletLog.setToName(appUserCard.getCardNo());
			walletLog.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogDao.updateById(walletLog);
			//处理邀请人佣金
            this.brokerageTopUp(appUserCard, transaction, walletLog);
			//消息处理
            this.sendWalletMsg(userEntity,walletLog);
		}
	}
	
	public void sendWalletMsg(AppUserEntity userEntity, WalletLogEntity walletLog) {
		if (userEntity != null) {
			BigDecimal realMoney = walletLog.getRealMoney() != null ? walletLog.getRealMoney() : BigDecimal.ZERO;
			BigDecimal serviceCharge = walletLog.getServiceCharge() != null ? walletLog.getServiceCharge()
					: BigDecimal.ZERO;
			messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.WALLET_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					userEntity.getUid(), 
					"-" + (realMoney.add(serviceCharge)) + TransCurrencyEnums.USD.getName(),
					userEntity.getWalletBalance() + TransCurrencyEnums.USD.getName(),
					userEntity.getFreezeBalance() + TransCurrencyEnums.USD.getName());
		}
	}
	
	
	/**
	 * @category 查询银行卡余额，修改数据库卡余额
	 * @param appUserCard
	 * @param transaction
	 */
	public void upUserCardBalance(AppUserCardEntity appUserCard, CardTransactionEntity transaction) {
		UserBankcardEntity bankcardEntity = new UserBankcardEntity();
		bankcardEntity.setUserBankcardId(appUserCard.getUserBankcardId());
		ResponseBase base = UCardApi.postData(appUserCard.getUid(), UCardMethods.QUERY_BANKCARD_BALANCE, bankcardEntity,
				null);
		Double balance = 0.0;
		if (base.getRtncode() == Constants.HTTP_RES_CODE_200) {
			String apiData = base.getData().toString();
			CardBalanceEntity balanceEntity = JSONObject.parseObject(apiData, CardBalanceEntity.class);
			balance = Double.parseDouble(balanceEntity.getBalance());
			appUserCard.setBalance(Double.parseDouble(balanceEntity.getBalance()));
			appUserCardDao.updateById(appUserCard);
			if (transaction != null) {
				//消息处理
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.TOP_UP_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						appUserCard.getUid(), 
						appUserCard.getCardNo(), transaction.getLocalCurrencyAmt() + TransCurrencyEnums.USD.getName(),
						balance + TransCurrencyEnums.USD.getName());
			}
		}
	}

	// 交易记录回调
	public ApiResponseEntity transactionCreated(CardRechargeResult entity) {
		try {
			System.out.println("***********************交易回调************************");
			if (entity == null || entity.getTransaction() == null || entity.getUserBankcardId() == null) {
				log.warn("交易回调参数不完整，忽略");
				return ApiResponseEntity.success();
			}
			CardRechargeResult.transaction tx = entity.getTransaction();
			Integer apiTransType = tx.getTransType();
			String transType = TransTypeEnums.getType(apiTransType);
			String amountRaw = tx.getLocalCurrencyAmt();
			// 未知交易类型（开卡噪声等）不入库，避免出现无类型/0元脏数据
			if (transType == null) {
				log.warn("交易回调未知transType={}, transactionId={}, userBankcardId={}, 跳过入库",
						apiTransType, tx.getTransactionId(), entity.getUserBankcardId());
				return ApiResponseEntity.success();
			}
			// 仅处理消费/退款；金额无效也不入库
			if (!TransTypeEnums.AUTH.getName().equals(transType)
					&& !TransTypeEnums.REFUND.getName().equals(transType)) {
				log.warn("交易回调非消费/退款类型 transType={}, 跳过入库", transType);
				return ApiResponseEntity.success();
			}
			if (amountRaw == null || amountRaw.trim().isEmpty()) {
				log.warn("交易回调金额为空 transactionId={}, 跳过入库", tx.getTransactionId());
				return ApiResponseEntity.success();
			}
			double amountVal;
			try {
				amountVal = Math.abs(Double.parseDouble(amountRaw.trim()));
			} catch (NumberFormatException ex) {
				log.warn("交易回调金额非法 amount={}, transactionId={}, 跳过入库",
						amountRaw, tx.getTransactionId());
				return ApiResponseEntity.success();
			}
			if (amountVal <= 0) {
				log.warn("交易回调金额为0 transactionId={}, 跳过入库", tx.getTransactionId());
				return ApiResponseEntity.success();
			}
			// 幂等：同一三方交易号不重复入库
			if (tx.getTransactionId() != null && !tx.getTransactionId().trim().isEmpty()) {
				CardTransactionEntity exists = cardTransactionDao.findOrderNum(tx.getTransactionId());
				if (exists != null) {
					log.info("交易回调已存在 transactionId={}, 跳过", tx.getTransactionId());
					return ApiResponseEntity.success();
				}
			}

			AppUserCardEntity cardEntity = appUserCardDao.findUserBankcardId(entity.getUserBankcardId());
			if (cardEntity == null) {
				return ApiResponseEntity.error();
			}
			CardTransactionEntity transaction = new CardTransactionEntity();
			transaction.setCardId(cardEntity.getCardId());
			transaction.setCardUuid(cardEntity.getCardUuid());
			transaction.setCardNo(cardEntity.getCardNo());
			transaction.setUid(cardEntity.getUid());
			if (entity.getCreateAt() != null && entity.getCreateAt() > 0) {
				transaction.setCreateAt(new Date(entity.getCreateAt()));
				transaction.setSetTime(new Date(entity.getCreateAt()));
			} else {
				transaction.setSetTime(new Date());
			}
			transaction.setLocalCurrency(tx.getLocalCurrency());
			String statusName = OrderStateEnums.getIdToName(tx.getTransStatus());
			transaction.setOrderState(statusName);
			transaction.setRequestOrderId(tx.getTransactionId());
			transaction.setTransCurrency(tx.getLocalCurrency());
			transaction.setTransType(transType);
			if (TransTypeEnums.AUTH.getName().equals(transType)) {
				transaction.setLocalCurrencyAmt("-" + amountRaw.trim());
				transaction.setTransCurrencyAmt("-" + amountRaw.trim());
			} else {
				transaction.setLocalCurrencyAmt("+" + amountRaw.trim());
				transaction.setTransCurrencyAmt("+" + amountRaw.trim());
			}
			transaction.setUserBankcardId(entity.getUserBankcardId());
			String lastFourDigits = cardEntity.getCardNo() != null && cardEntity.getCardNo().length() >= 4
					? cardEntity.getCardNo().substring(cardEntity.getCardNo().length() - 4)
					: cardEntity.getCardNo();
			if (tx.getMerchantName() != null && !tx.getMerchantName().isEmpty()) {
				transaction.setTitle("Card:" + lastFourDigits + " " + tx.getMerchantName());
			}
			if (tx.getFeeAmount() != null && !tx.getFeeAmount().trim().isEmpty()) {
				try {
					transaction.setHandlingFees(Math.abs(Double.parseDouble(tx.getFeeAmount().trim())));
				} catch (NumberFormatException ignore) {
					transaction.setHandlingFees(0.0);
				}
			} else {
				transaction.setHandlingFees(0.0);
			}
			transaction.setGmtModified(new Date());
			cardTransactionDao.insert(transaction);

			if (OrderStateEnums.POSTED.getName().equals(statusName)) {
				UserBankcardEntity bankcardEntity = new UserBankcardEntity();
				bankcardEntity.setUserBankcardId(transaction.getUserBankcardId());
				ResponseBase base = UCardApi.postData(cardEntity.getUid(), UCardMethods.QUERY_BANKCARD_BALANCE,
						bankcardEntity, null);
				Double balance = 0.0;
				if (base.getRtncode() == Constants.HTTP_RES_CODE_200 && base.getData() != null) {
					CardBalanceEntity balanceEntity = JSONObject.parseObject(base.getData().toString(),
							CardBalanceEntity.class);
					if (balanceEntity != null && balanceEntity.getBalance() != null) {
						balance = Double.parseDouble(balanceEntity.getBalance());
						cardEntity.setBalance(balance);
						appUserCardDao.updateById(cardEntity);
					}
				}
				String feeStr = tx.getFeeAmount() == null || tx.getFeeAmount().trim().isEmpty()
						? "0" : tx.getFeeAmount().trim();
				double feeVal = 0.0;
				try {
					feeVal = Math.abs(Double.parseDouble(feeStr));
				} catch (NumberFormatException ignore) {
					feeVal = 0.0;
				}
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.CONSUME_ZH.getIndex(),
						LanguageContext.getLanguage(),
						cardEntity.getUid(),
						cardEntity.getCardNo(),
						amountRaw.trim() + TransCurrencyEnums.USD.getName(),
						feeVal + TransCurrencyEnums.USD.getName(),
						balance + TransCurrencyEnums.USD.getName());
			}
			return ApiResponseEntity.success();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category 交易验证码
	 * @param result
	 * @return
	 */
	public ApiResponseEntity transactionVerificationCode(CardRechargeResult result) {
		System.out.println("******************交易验证码回到********************");
		AppUserCardEntity userCard = appUserCardDao.findUserBankcardId(result.getUserBankcardId());
		if (userCard != null && result.getOtp() != null && result.getOtp().length() > 0) {
			//消息处理
			messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.TRANSACTION_CODE_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					userCard.getUid(),
					result.getOtp());
			return ApiResponseEntity.success();
		}
		return ApiResponseEntity.error();
	
	}

	public ApiResponseEntity cardStateUp(CardRechargeResult resData) {
		// 卡片激活状态回调
		if (resData != null && CardStateEnums.ACTIVE.getLable().equals(resData.getStatus())) {
			return this.activeCard(resData);
		} else if (resData != null && CardStateEnums.CANCELLATION.getLable().equals(resData.getStatus())) {// 卡片关闭状态回调
			return this.closeCardRes(resData);
		} else if (resData != null && CardStateEnums.FREEZE.getLable().equals(resData.getStatus())) {// 卡片冻结状态回调
			return this.cardFreeze(resData);//银行卡冻结
		}
		return ApiResponseEntity.error();
	}

	// 卡片关闭状态回调
	private ApiResponseEntity closeCardRes(CardRechargeResult resData) {
		AppUserCardEntity appUserCard = appUserCardDao.findUserBankcardId(resData.getUserBankcardId());
		CardCloseEntity cardCloseEntity = cardCloseDao.findUserBankCardId(resData.getUserBankcardId());
		if(appUserCard != null && cardCloseEntity != null) {
			// 修改记录中的状态
			cardCloseEntity.setRefundAmt(cardCloseEntity.getBalance());
			cardCloseEntity.setReviewStatus(ReviewStatusEnums.SUCCESS.getIndex());
			cardCloseDao.updateById(cardCloseEntity);
			// 修改银行卡状态
			AppUserCardEntity userCardEntity = appUserCardDao
					.findUserBankcardId(cardCloseEntity.getUserBankcardId());
			if (userCardEntity != null) {
				userCardEntity.setBalance(0.0);
				userCardEntity.setStatus(CardStateEnums.CANCELLATION.getIndex().toString());
				appUserCardDao.updateById(userCardEntity);
			}
			// 修改用户钱包余额
			Double lodMoney = 0.0;
			AppUserEntity userEntity = appUserDao.findByUid(cardCloseEntity.getUid());
			if (userEntity != null) {
				lodMoney = userEntity.getWalletBalance();
				userEntity.setWalletBalance(userEntity.getWalletBalance() + cardCloseEntity.getBalance());
				appUserDao.updateById(userEntity);
			}
			// 新增记录
			this.closeCardLong(cardCloseEntity, userEntity, lodMoney);
			//消息处理
			messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.CARD_CLOSE_SUCCESS_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					userEntity.getUid(), 
					userCardEntity.getCardNo());
			return ApiResponseEntity.success();
		}
		return ApiResponseEntity.error();
	}
	
	public void closeCardLong(CardCloseEntity cardCloseEntity, AppUserEntity userEntity, Double lodMoney) {
		try {
			// 新增银行卡转账记录
			CardTransactionEntity transaction = new CardTransactionEntity();
			transaction.setUid(cardCloseEntity.getUid());
			transaction.setUserBankcardId(cardCloseEntity.getUserBankcardId());
			transaction.setCardId(cardCloseEntity.getCardId());
			transaction.setCardUuid(cardCloseEntity.getCardUuid());
			transaction.setCardNo(cardCloseEntity.getCardNo());
			transaction.setHandlingFees(0.0);
			transaction.setRequestOrderId(
					OrderCodeFactory.getOrderCode(Long.parseLong(cardCloseEntity.getUserBankcardId().toString())));
			transaction.setOrderState(OrderStateEnums.POSTED.getName());
			transaction.setLocalCurrency(TransCurrencyEnums.USD.getName());
			transaction.setLocalCurrencyAmt("-" + cardCloseEntity.getBalance());
			transaction.setTransCurrency(TransCurrencyEnums.USD.getName());
			transaction.setTransCurrencyAmt("-" + cardCloseEntity.getBalance());
			transaction.setTransType(TransTypeEnums.CLOSE.getName());
			GenericityUtil.setDate(transaction);
			cardTransactionDao.insert(transaction);
			// 新增钱包转账记录
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(cardCloseEntity.getUid())));
			walletLogEntity.setOutOrderNo(
					OrderCodeFactory.getOrderCode(Long.parseLong(cardCloseEntity.getUserBankcardId().toString())));
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
			walletLogEntity.setTitle(WalletTitleTypeEnums.CARD_CLOSE.getCnName());
			walletLogEntity.setPrimevalMoney(lodMoney);
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(cardCloseEntity.getBalance()));
			walletLogEntity.setFormName(cardCloseEntity.getCardNo());
			walletLogEntity.setFormAccount(cardCloseEntity.getCardNo());
			walletLogEntity.setToName(userEntity.getUserEmail());
			walletLogEntity.setToAccount(userEntity.getUserEmail());
			walletLogEntity.setUserBankcardId(cardCloseEntity.getUserBankcardId());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.CARD_CLOSE.getIndex());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	
	// 银行卡激活回调
	public ApiResponseEntity activeCard(CardRechargeResult resData) {
		AppUserCardEntity appUserCard = appUserCardDao.findUserBankcardId(resData.getUserBankcardId());
		if (appUserCard != null) {
			CardInfoEntity cardInfoEntity = new CardInfoEntity();
			cardInfoEntity.setUserBankcardId(resData.getUserBankcardId());
			ResponseBase base = UCardApi.postData(appUserCard.getUid(), UCardMethods.BANKCARD_INFO, cardInfoEntity,
					null);
			if (!Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
				return ApiResponseEntity.error();
			}
			
			// ========== 添加状态查询重试逻辑 ==========
			CardInfoEntity infoEntity = JSONObject.parseObject(base.getData().toString(), CardInfoEntity.class);
			int retryCount = 0;
			int maxRetriesNum = 5;        // 最大重试次数
			long retryIntervalTime = 3000;   // 重试间隔（毫秒）
			// 如果状态不是激活，进行重试
			while (!CardStateEnums.ACTIVE.getIndex().equals(infoEntity.getStatus()) && retryCount < maxRetriesNum) {
				retryCount++;
				log.warn("卡未激活，{}秒后进行第{}次重试, userBankcardId: {}, 当前状态: {}", 
						retryIntervalTime / 1000, retryCount, resData.getUserBankcardId(), infoEntity.getStatus());
				
				try {
					Thread.sleep(retryIntervalTime);
				} catch (InterruptedException e) {
					log.error("重试等待被中断", e);
					Thread.currentThread().interrupt();
					return ApiResponseEntity.error();
				}
				
				// 重新查询卡状态
				base = UCardApi.postData(appUserCard.getUid(), UCardMethods.BANKCARD_INFO, cardInfoEntity, null);
				if (!Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
					log.error("重试查询卡状态接口失败, code: {}, 重试次数: {}", base.getRtncode(), retryCount);
					continue;  // 接口调用失败，继续重试
				}
				infoEntity = JSONObject.parseObject(base.getData().toString(), CardInfoEntity.class);
			}
			
			// 重试结束后，如果状态还不是激活，返回错误
			if (!CardStateEnums.ACTIVE.getIndex().equals(infoEntity.getStatus())) {
				log.error("卡状态查询重试{}次后仍未激活, userBankcardId: {}, 最终状态: {}", 
						maxRetriesNum, resData.getUserBankcardId(), infoEntity.getStatus());
				return ApiResponseEntity.error();
			}
			// ========== 重试逻辑结束 ==========
			
			// 修改申请记录状态
			CardApplyEntity applyEntity = cardApplyDao.selectById(appUserCard.getApplyId());
			applyEntity.setApplyState(OpenCardStateEnums.SUCCESS_ACTIVATION.getIndex());
			applyEntity.setApplyStateName(OpenCardStateEnums.SUCCESS_ACTIVATION.getName());
			cardApplyDao.updateById(applyEntity);
			// 修改银行卡状态银行卡号
			appUserCard.setCardNo(resData.getCardNo());
			appUserCard.setStatus(CardStateEnums.ACTIVE.getIndex().toString());
			appUserCardDao.updateById(appUserCard);

			AppUserEntity userEntity = appUserDao.findByUid(applyEntity.getUid());
			if (userEntity != null) {
				if (CardTypeEnums.VIRTUAL.getName().equals(appUserCard.getCardType())) {// 虚拟卡激活成功释放冻结，实体卡发货时处理
					// 释放冻结
//					userEntity.setOpenFreezeBalance(userEntity.getOpenFreezeBalance() - applyEntity.getOpenCardTotal());
//					appUserDao.updateById(userEntity);
				} else if (CardTypeEnums.PHYSICAL.getName().equals(appUserCard.getCardType())) {
					// 设置pin码
					if (appUserCard.getPinNum() != null && appUserCard.getPinNum().length() > 0) {
						cardServiceImpl.setPin(appUserCard.getUid(), appUserCard.getUserBankcardId(),
								appUserCard.getPinNum());
					}
					//修改开卡记录状态
					AppUserCardOpenEntity appUserCardOpenEntity = appUserCardOpenDao.findState(appUserCard.getUid(), appUserCard.getUserBankcardId(), PublicEnums.ONE.getIndex());
					if(appUserCardOpenEntity != null) {
						appUserCardOpenEntity.setOpenState(PublicEnums.TOW.getIndex());
						appUserCardOpenDao.updateById(appUserCardOpenEntity);
					}
				}
			}
			//消息处理
			messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.OPEN_CARD_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					applyEntity.getUid(),
					appUserCard.getCardNo());
			return ApiResponseEntity.success();
		}
		return ApiResponseEntity.error();
	}
	
	
	
	// 银行卡冻结回调
	public ApiResponseEntity cardFreeze(CardRechargeResult resData) {
		AppUserCardEntity appUserCard = appUserCardDao.findUserBankcardId(resData.getUserBankcardId());
		if(appUserCard != null) {
			appUserCard.setStatus(CardStateEnums.FREEZE.getIndex().toString());
			appUserCardDao.updateById(appUserCard);
			return ApiResponseEntity.success();
		}
		return ApiResponseEntity.error();
	}
	
	
	public ApiResponseEntity kycStatusChange(CardRechargeResult result) {
		System.out.println("********************kyc认证状态回调**********************");
		AppUserEntity appUserEntity = appUserDao.findByUid(result.getUid().toString());
		if(appUserEntity != null) {
			if(PublicEnums.ONE.getName().equals(result.getAuditState())) {//认证通过
				appUserEntity.setKycState(KycStateEnums.SUCCESS_APPROVE.getIndex());
				appUserEntity.setKycStateName(KycStateEnums.SUCCESS_APPROVE.getName());
				appUserDao.updateById(appUserEntity);
				this.sendKycMsg(MessageEnums.KYC_SUCCESS_ZH.getIndex(),
						appUserEntity.getUid(),
						null,
						appUserEntity.getUserEmail());
			}else if(PublicEnums.TOW.getName().equals(result.getAuditState())) {//认证不通过
				appUserEntity.setKycState(KycStateEnums.ERROR_APPROVE.getIndex());
				appUserEntity.setKycStateName(KycStateEnums.ERROR_APPROVE.getName());
				appUserEntity.setKycAuditResult(result.getAuditRemark());
				appUserDao.updateById(appUserEntity);
				this.sendKycMsg(MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(),
						appUserEntity.getUid(),
						result.getAuditRemark(),
						appUserEntity.getUserEmail());

			}
		}
		
		return ApiResponseEntity.success();
	}
	
	public void sendKycMsg(Integer msgCode,String uid,String msgInfo,String userEmail) {
		try {
			if(msgInfo != null && msgInfo.length() > 0) {//认证不通过
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						uid,
						msgInfo);
				//飞机消息
				messageTemplateServiceImpl.robotSendAsync(
						MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(),
						LanguageEnums.ZH_CN.getName(),
						RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
						uid,
						msgInfo);
			}else {//认证通过
				//消息处理
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.KYC_SUCCESS_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						uid);
				//飞机消息
				messageTemplateServiceImpl.robotSendAsync(
						MessageEnums.ROBOT_KYC_SUCCESS_ZH.getIndex(),
						LanguageEnums.ZH_CN.getName(),
						RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
						userEmail,
						uid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category 钱包地址更换
	 */
	@Override
    public ResponseBase resetAddres(ResetAddressEntity entity, HttpServletRequest req) {
        try {
            // 获取客户端 IP
            String ipAddress = ipUtil.getClientIp(req);
            if (Constants.MSG_IP_LIST.contains(ipAddress) || ipUtil.isLocalIp(ipAddress)) {
                // 验签
                String sign = entity.getSign();
                entity.setSign(null);
                boolean temp = WalletApi.Verification(entity, sign);
                if (temp) {
                	AppUserEntity userEntity = appUserDao.findAddress(entity.getOriginalAddress());
                    if (userEntity != null) {
//                        userEntity.setTronUsdtAcount(entity.getNewAddress());
//                        appUserDao.updateById(userEntity);
                    	//消息处理
                		messageTemplateServiceImpl.unifiedSendAsync(
                				MessageEnums.WALLET_RESET_ADDRES_ZH.getIndex(), 
                				LanguageContext.getLanguage(),
                				userEntity.getUid(),
                				entity.getOriginalAddress(),
                				entity.getNewAddress());
                        return setResultSuccess();
                    } else {
                        return setResultError("原始地址不存在");
                    }
                } else {
                    return setResultError("验签不通过");
                }
            } else {
                return setResultError("IP地址没有访问权限");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

	
	//银行卡充值回调佣金处理
    public void brokerageTopUp(AppUserCardEntity appUserCard, CardTransactionEntity transaction, WalletLogEntity walletLog) {
        AppUserIntiterEntity intiterEntity = appUserIntiterDao.findCoverInviterUserUid(appUserCard.getUid());
        //上级渠道上id
        Integer intiterUserId;
        if(IntiterUserTypeEnums.CHANNEL_USER.getIndex().equals(intiterEntity.getIntiterUserType()) ) {//邀请人是渠道商
        	intiterUserId =intiterEntity.getInviterUser();
        }else {//如果上级不是渠道商，那么递归查询到type是渠道商的邀请人
        	intiterUserId = this.findChannelSuperiorByInviteeRecursive(appUserCard.getUid());
        }
        //存在上级渠道商
        if(intiterUserId != null) {
        	//查询当前渠道商信息
        	ChannelUserEntity channelUserEntity = channelUserDao.selectById(intiterUserId);
            if(channelUserEntity == null) {
            	channelUserEntity = appChannelIntiterDao.findByappId(intiterUserId);
            }
            if(channelUserEntity != null) {
            	List<ChannelUserLeve> channelUserLeves = new ArrayList<ChannelUserLeve>();
            	ChannelUserLeve channelUserLeve = new ChannelUserLeve();
            	channelUserLeve.setCurrentUserId(intiterUserId);
            	channelUserLeve.setCurrentUserUId(channelUserEntity.getUserId());
                channelUserLeve.setCurrentLevel(channelUserEntity.getLevelNo());
                channelUserLeve.setCurrentLevelName(channelUserEntity.getLevelName());
                channelUserLeve.setLeveId(PublicEnums.ONE.getIndex());
                //查询用户佣金比例
                //BigDecimal decimal  = channelLevelDao.findRate(channelUserEntity.getLevelNo());
                Double commissionRate = channelUserEntity.getCommissionRate() == null ? 0: channelUserEntity.getCommissionRate();
                BigDecimal decimal = BigDecimal.valueOf(commissionRate);
                channelUserLeve.setRatesFee(decimal);
                channelUserLeve.setActualRatesFee(decimal);
                channelUserLeves.add(channelUserLeve);
            	this.findChanneSuperiorLeve3(channelUserEntity.getUserId().toString(), channelUserLeves, PublicEnums.ONE.getIndex(),decimal);
            	//计算佣金
            	BigDecimal realMoney = walletLog.getRealMoney() == null
                          ? BigDecimal.ZERO
                          : walletLog.getRealMoney();

                BigDecimal serviceCharge = walletLog.getServiceCharge() == null
                          ? BigDecimal.ZERO
                          : walletLog.getServiceCharge();

                BigDecimal monery = realMoney.add(serviceCharge);
            	for (int i = 0; i < channelUserLeves.size(); i++) {
            		 BigDecimal brokerageFee = monery.multiply(channelUserLeves.get(i).getActualRatesFee()).setScale(2, RoundingMode.HALF_UP);
            		 //新增佣金记录
            		 this.addChannalBrokerageLog(brokerageFee,transaction,channelUserLeves.get(i),intiterEntity,walletLog);
    			}
            }
        }
    }
	
    /**
     * 通过被邀请人uid递归查询，直到找到渠道商(intiterUserType=2)为止
     * 
     * @param coverInviterUserUid 被邀请人的uid
     * @return 渠道商的id (InviterUser)，如果未找到则返回null
     */
    public Integer findChannelSuperiorByInviteeRecursive(String coverInviterUserUid) {
        // 参数校验
        if (coverInviterUserUid == null || coverInviterUserUid.trim().isEmpty()) {
            return null;
        }
        // 查询当前被邀请人的邀请记录
        AppUserIntiterEntity record = appUserIntiterDao.findCoverInviterUserUid(coverInviterUserUid);
        // 没有找到邀请记录，说明没有上级
        if (record == null) {
            return null;
        }
        // 检查邀请人类型是否为渠道商(2)
        if (record.getIntiterUserType() != null && record.getIntiterUserType() == 2) {
            // 找到渠道商，返回邀请人id
            return record.getInviterUser();
        }
        // 不是渠道商，继续向上递归查询
        // 将当前邀请人的uid作为新的被邀请人uid继续查询
        return findChannelSuperiorByInviteeRecursive(record.getInviterUserUid());
    }
    
    /**
     * @category 查询每级渠道商，封装等级及对应的佣金比例
     * @param coverInviterUser
     * @param channelUserLeves
     * @param level
     * @param rateFee
     * @return
     */
    public List<ChannelUserLeve> findChanneSuperiorLeve3(String coverInviterUser,List<ChannelUserLeve> channelUserLeves,Integer level, BigDecimal rateFee) {
        // 参数校验
        if (coverInviterUser == null) {
            return channelUserLeves;
        }
        // 查询当前被邀请人的邀请记录
        AppUserIntiterEntity record = appUserIntiterDao.findByUidCoverInviterUser(coverInviterUser.toString());
        // 没有找到邀请记录，说明没有上级
        if (record == null) {
            return channelUserLeves;
        }
        BigDecimal decimal = BigDecimal.ZERO;
        // 检查邀请人类型是否为渠道商(2)
        if (record.getIntiterUserType() != null && record.getIntiterUserType() == 2) {
        	 ChannelUserLeve channelUserLeve = new ChannelUserLeve();
             channelUserLeve.setCurrentUserId(record.getInviterUser());
             //查询当前渠道商信息
             ChannelUserEntity channelUserEntity = channelUserDao.selectById(record.getInviterUser());
             if(channelUserEntity != null) {
            	 channelUserLeve.setCurrentUserUId(channelUserEntity.getUserId());
                 channelUserLeve.setCurrentLevel(channelUserEntity.getLevelNo());
                 channelUserLeve.setCurrentLevelName(channelUserEntity.getLevelName());
                 channelUserLeve.setLeveId(level + 1);
                 //查询用户的佣金比例
                 //decimal  = channelLevelDao.findRate(channelUserEntity.getLevelNo());
                 decimal = BigDecimal.valueOf(channelUserEntity.getCommissionRate());
                 channelUserLeve.setRatesFee(decimal);
                 channelUserLeve.setActualRatesFee(decimal.subtract(rateFee));
                 channelUserLeves.add(channelUserLeve);
                 level+=1;
             }
        }
        // 不是渠道商，继续向上递归查询
        return findChanneSuperiorLeve3(record.getInviterUserUid(),channelUserLeves,level,decimal);
    }
    
    /**
     * @category 新增佣金记录
     * @param brokerageFee
     * @param transaction
     * @param channelUserLeve
     * @param intiterEntity
     * @param walletLog
     */
    public void addChannalBrokerageLog(BigDecimal brokerageFee, CardTransactionEntity transaction,
			ChannelUserLeve channelUserLeve, AppUserIntiterEntity intiterEntity, WalletLogEntity walletLog) {
		// 新增佣金记录
		try {
			BrokerageLogEntity brokerageLog = new BrokerageLogEntity();
			brokerageLog.setOrderId(
					OrderCodeFactory.getOrderCode(Long.parseLong(channelUserLeve.getCurrentUserId().toString())));
			brokerageLog.setIntiterUserType(PublicEnums.TOW.getIndex());
			brokerageLog.setIntiterUserLevel(channelUserLeve.getCurrentLevel());
			brokerageLog.setInviterUser(channelUserLeve.getCurrentUserId());
			brokerageLog.setInviterUserUid(channelUserLeve.getCurrentUserUId().toString());
			brokerageLog.setCoverInviterUser(intiterEntity.getCoverInviterUser());
			brokerageLog.setCoverInviterUserUid(intiterEntity.getCoverInviterUserUid());
			brokerageLog.setCardId(transaction.getCardId());
			brokerageLog.setCardUuid(transaction.getCardUuid());
			CardEntity cardEntity = cardDao.findByuuId(transaction.getCardUuid());
			if (cardEntity != null) {
				brokerageLog.setCardType(cardEntity.getBankCardNature());
			}
			brokerageLog.setTopupFee(walletLog.getRealMoney());
			brokerageLog.setHandlingFee(walletLog.getServiceCharge());
			brokerageLog.setBrokerageRates(channelUserLeve.getActualRatesFee());
			brokerageLog.setBrokerageFee(brokerageFee);
			GenericityUtil.setDate(brokerageLog);
			brokerageLogDao.insert(brokerageLog);
			//新增用户佣金金额
			AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.findChannalId(channelUserLeve.getCurrentUserUId());
	        if (appChannelIntiterEntity != null) {
	            AppUserEntity appUserEntity = appUserDao.selectById(appChannelIntiterEntity.getAppId());
	            if (appUserEntity != null) {
	                Double withdrawableBalance = appUserEntity.getWithdrawableBalance();
	                appUserEntity.setWithdrawableBalance(withdrawableBalance + brokerageFee.doubleValue());
	                appUserEntity.setGmtModified(new Date());
	                appUserDao.updateById(appUserEntity);
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
    
 	/**
 	 * @category 商户充值回调
 	 * @param result
 	 * @return
 	 */
 	public ApiResponseEntity merchantRecharge(CardRechargeResult result) {
 		System.out.println("********************商户充值回调**********************");
 		//飞机消息
		messageTemplateServiceImpl.robotSendAsync(
				MessageEnums.ROBOT_RECHARGE_HISTORY_ZH.getIndex(),
				LanguageEnums.ZH_CN.getName(),
				RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
				result.getAmount(),
				result.getTxHash(),
				result.getTxTime());
 		return ApiResponseEntity.success();
 	}
    
    
	@Override
	public ResponseBase info(String uid, Integer userBankcardId) {
		CardInfoEntity cardInfoEntity = new CardInfoEntity();
		cardInfoEntity.setUserBankcardId(userBankcardId);
		ResponseBase base = UCardApi.postData(uid, UCardMethods.QUERY_BANKCARD_BALANCE, cardInfoEntity,
				null);
		System.out.println(JSON.toJSON(base));
		return base;
	}

	@Override
	public ResponseBase state(String uid, Integer userBankcardId) {
		CardInfoEntity cardInfoEntity = new CardInfoEntity();
		cardInfoEntity.setUserBankcardId(userBankcardId);
		ResponseBase base = UCardApi.postData(uid, UCardMethods.BANKCARD_INFO, cardInfoEntity,
				null);
		System.out.println(JSON.toJSON(base));
		return base;
	}
	

}
