package com.worldpay.service.impl;

import com.worldpay.utils.api.WalletApi;
import com.worldpay.utils.api.UCardApi;
import com.worldpay.utils.api.AesAPIUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.dto.InviterLevelDTO;
import com.worldpay.api.request.*;
import com.worldpay.api.response.*;
import com.worldpay.api.response.TrackingInfoEntity.AcceptedItem;
import com.worldpay.api.response.TrackingInfoEntity.Event;
import com.worldpay.api.response.TrackingInfoEntity.Provider;
import com.worldpay.api.response.TrackingInfoEntity.Tracking;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.constants.UsdtTopinMethods;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserCardDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserFileDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.brokerage.ChannelLevelDao;
import com.worldpay.dao.card.*;
import com.worldpay.dao.channel.ChannelCardDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.template.EmailTemplateDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.*;
import com.worldpay.entity.brokerage.ChannelLevelEntity;
import com.worldpay.entity.card.*;
import com.worldpay.entity.channel.ChannelCardEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.system.EmailTemplateEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.*;
import com.worldpay.query.CardApplyQueryEntity;
import com.worldpay.service.CardService;
import com.worldpay.utils.*;
import lombok.Synchronized;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.worldpay.constants.Constants.HTTP_RES_CODE_200;

@Service
@Transactional
public class CardServiceImpl extends BaseApiService implements CardService {

	@Value("${EMS_API_KEY:}")
	private String EMS_API_KEY;
	
	@Value("${bank.aesKey:}")
	private String APPS_ECRET;
	
	@Autowired
	private ChannelUserDao channelUserDao;

	@Autowired
	private ChannelLevelDao channelLevelDao;

	@Autowired
	private AppChannelIntiterDao appChannelIntiterDao;
	
	@Autowired
	private CardDao cardDao;

	@Autowired
	private CardLableDao cardLableDao;

	@Autowired
	private CardLableJoinDao cardLableJoinDao;

	@Autowired
	private CardSynopsisDao cardSynopsisDao;

	@Autowired
	private CardSynopsisJoinDao cardSynopsisJoinDao;

	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private AppUserHolderDao appUserHolderDao;

	@Autowired
	private CardApplyDao cardApplyDao;

	@Autowired
	private CardApplySendDao cardApplySendDao;

	@Autowired
	private CardApplyManDao cardApplyManDao;
	
	@Autowired
	private CardApplyKycDao cardApplyKycDao;
	
	@Autowired
	private AppUserCardDao appUserCardDao;

	@Autowired
	private CardTransactionDao cardTransactionDao;
	
	@Autowired
	private WalletLogDao walletLogDao;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;
	
	@Autowired
	private CardShippingDao cardShippingDao;
	
	@Autowired
	private AppUserIntiterDao appUserIntiterDao;
	
	@Autowired
	private ChannelCardDao channelCardDao;
	
	@Autowired
	private AppUserFileDao appUserFileDao;
	
	@Autowired
	private AppUserCardOpenDao appUserCardOpenDao;
	
	@Autowired
	private CardholderServiceImpl cardholderServiceImpl;
	
	@Autowired
	private CardApplyServiceImpl applyServiceImpl;
	
	@Autowired
	private IpUtil ipUtil;
	
    @Value("${invitation.default_code:}")
    private String INVITATION_DEFAULT_CODE;
	

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "get", remark = "拉取三方银行卡信息列表入库")
	public ResponseBase pullList() {
		try {
			ResponseBase resData = UCardApi.postData(null, UCardMethods.MERCHANT_BANKCARD_TEMPLATE_LIST, null, null);
			if (!Constants.HTTP_RES_CODE_200.equals(resData.getRtncode()) || resData.getData() == null) {
				return resData;
			}
			JSONArray array = JSONArray.parseArray(resData.getData().toString());
			List<CardEntity> list = new ArrayList<>();
			if (array != null && !array.isEmpty()) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject item = array.getJSONObject(i);
					Integer productId = item.getInteger("id");
					if (productId == null) {
						continue;
					}
					CardEntity exists = cardDao.findById(productId);
					if (exists != null) {
						continue;
					}
					CardEntity entity = new CardEntity();
					entity.setId(productId);
					entity.setTitle(item.getString("cardTitle"));
					entity.setCardBin(item.getString("cardBin"));
					entity.setBankCardNature(item.getString("bankCardNature"));
					entity.setBankCardType(item.getString("cardBrand"));
					entity.setCardBrand(item.getString("cardBrand"));
					entity.setCcy(item.getString("ccy"));
					entity.setApplyFee(item.getDouble("applyFee"));
					entity.setOpenCardCost(item.getDouble("applyFee"));
					entity.setRechargeFee(item.getDouble("rechargeFee"));
					entity.setActiveMinLimit(item.getDouble("activeMinLimit"));
					entity.setRechargeMinLimit(item.getDouble("rechargeMinLimit"));
					entity.setCardState(PublicEnums.ONE.getIndex());
					entity.setMonthFee(0);
					entity.setRechargeMaxLimit(1000000.0);
					entity.setUuid(UUID.randomUUID().toString().replace("-", ""));
					cardDao.insert(entity);
					list.add(entity);
				}
			}
			return setResultSuccess(list, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "post", remark = "银行卡信息列表")
	public ResponseBase findList(CardEntity entity) {
		try {
			entity.setLanguage(LanguageContext.getLanguage());
			entity.setCardState(PublicEnums.ONE.getIndex());
			List<CardEntity> list = cardDao.findList(entity);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					// 标签列表
					List<CardLableEntity> lableList = cardLableDao.findByCardId(list.get(i).getUuid(),
							entity.getLanguage());
					list.get(i).setLableList(lableList);
					// 卡片简介
					CardSynopsisEntity synopsisEntity = cardSynopsisDao.findByCardId(list.get(i).getUuid(),
							entity.getLanguage());
					list.get(i).setSynopsisData(synopsisEntity);
				}
			}
			return setResultSuccess(list, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "post", remark = "编辑银行卡")
	public ResponseBase update(CardEntity entity) {
		try {
			CardEntity cardEntity = cardDao.findByuuId(entity.getUuid());
			if (cardEntity != null) {
				UpdateWrapper<CardEntity> updateWrapper = new UpdateWrapper<>();
				updateWrapper.eq("uuid", entity.getUuid());
				cardDao.update(entity, updateWrapper);
//				cardDao.updateByUuId(entity);
				if (entity.getLableIdList() != null && entity.getLableIdList().size() > 0) {
					cardLableJoinDao.deleteByCardId(entity.getUuid());
					for (int i = 0; i < entity.getLableIdList().size(); i++) {
						CardLableJoinEntity joinEntity = new CardLableJoinEntity();
						joinEntity.setCardId(entity.getUuid());
						joinEntity.setLableId(entity.getLableIdList().get(i));
						CardLableEntity lableEntity = cardLableDao.selectById(entity.getLableIdList().get(i));
						if(lableEntity != null) {
							joinEntity.setLanguage(lableEntity.getLanguage());							
						}
						GenericityUtil.setDate(joinEntity);
						cardLableJoinDao.insert(joinEntity);
					}
				}
				if (entity.getSynopsisIdList() != null && entity.getSynopsisIdList().size() > 0) {
					cardSynopsisJoinDao.deleteByCardId(entity.getUuid());
					for (int i = 0; i < entity.getSynopsisIdList().size(); i++) {
						CardSynopsisJoinEntity joinEntity = new CardSynopsisJoinEntity();
//						joinEntity.setLanguage(entity.getSynopsisReq().get(i).getLanguage());
						CardSynopsisEntity synopsisEntity = cardSynopsisDao.selectById(entity.getSynopsisIdList().get(i));
						if(synopsisEntity != null) {
							joinEntity.setLanguage(synopsisEntity.getLanguage());
						}
						joinEntity.setSynopsisId(entity.getSynopsisIdList().get(i));
						joinEntity.setCardId(entity.getUuid());
						GenericityUtil.setDate(joinEntity);
						cardSynopsisJoinDao.insert(joinEntity);
					}
				}
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "get", remark = "银行卡详情")
	public ResponseBase findById(String uuid) {
		try {
			String language = LanguageContext.getLanguage();
			CardEntity cardEntity = cardDao.findByuuId(uuid);
			if (cardEntity != null) {
				List<CardLableEntity> lableList = cardLableDao.findByCardId(cardEntity.getUuid(), language);
				cardEntity.setLableList(lableList);
				// 卡片简介
				CardSynopsisEntity synopsisEntity = cardSynopsisDao.findByCardId(cardEntity.getUuid(), language);
				cardEntity.setSynopsisData(synopsisEntity);
			}
			return setResultSuccess(cardEntity,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * 私有递归辅助方法（带层级限制）
	 * @param currentCoverUid 当前被邀请人uid
	 * @param currentLevel 当前层级（1/2/3）
	 */
	private InviterLevelDTO  recursiveQueryWithLevelLimit(String currentCoverUid, Integer currentLevel) {
		// 递归终止条件（二选一即可）：
		// 1. 当前层级>3（达到最大限制）
		// 2. 传入的被邀请人uid为null（无有效查询条件）
		if (currentLevel > 3 || currentCoverUid == null) {
			return null;
		}
		// 步骤1：查询当前被邀请人的直接上级记录
		AppUserIntiterEntity currentRecord = appUserIntiterDao.queryByCoverUid(currentCoverUid);
		// 步骤2：额外终止条件：无上级记录 或 无上级uid
		if (currentRecord == null || currentRecord.getInviterUserUid() == null) {
			return null;
		}
		if (currentRecord.getIntiterUserType().equals(IntiterUserTypeEnums.CHANNEL_USER.getIndex())){
			InviterLevelDTO dto = new InviterLevelDTO();
			dto.setInviterUid(currentRecord.getInviterUserUid());
			dto.setInviterType(currentRecord.getIntiterUserType());
			dto.setInviterLevel(currentRecord.getIntiterUserLevel());
			dto.setLevelNum(currentLevel);

			return dto;
		}
		// 步骤3：封装当前层级的上级信息
		InviterLevelDTO dto = new InviterLevelDTO();
		dto.setInviterUid(currentRecord.getInviterUserUid());
		dto.setInviterType(currentRecord.getIntiterUserType());
		dto.setInviterLevel(currentRecord.getIntiterUserLevel());
		dto.setLevelNum(currentLevel);
		// 步骤4：递归调用：层级+1，以上级uid作为新的被邀请人uid
		return recursiveQueryWithLevelLimit(currentRecord.getInviterUserUid(), currentLevel + 1);
	}

	/**
	 * 递归查询最多3级上级邀请人
	 * @param targetCoverUid 目标被邀请人uid
	 * @return 1-3级上级信息列表
	 */
	public InviterLevelDTO queryInviterType(String targetCoverUid) {
		// 调用递归辅助方法，初始层级为1（直接上级）
		return recursiveQueryWithLevelLimit(targetCoverUid, 1);
	}

//	@Override
//	@SysLogAnnotation(module = "银行卡管理", type = "post", remark = "申请开卡")
//	public ResponseBase openCardApply(CardApplyQueryEntity entity) {
//		try {
//			//支付密码校验
//			ResponseBase passwordBase = appUserServiceImpl.checkPayPasswrod(entity.getUid(), entity.getPayPassword());
//			if(!Constants.HTTP_RES_CODE_200.equals(passwordBase.getRtncode())) {
//				return passwordBase;
//			}
//			// 获取当前app用户
//			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
//			if (userEntity == null) {
//				return setResultError(I18nUtil.getMessage("account_error"));
//			}
//			// 获取申请银行卡信息
//			CardEntity cardEntity = cardDao.findByuuId(entity.getCardUuid());
//			if (cardEntity == null) {
//				return setResultError(I18nUtil.getMessage("bank_card_null"));
//			}
//			// 持卡人信息
//			AppUserHolderEntity cardholderEntity = cardholderDao.selectById(entity.getHolderId());
//			if (cardholderEntity == null) {
//				return setResultError(I18nUtil.getMessage("holder_null"));
//			}
//			//kyc认证信息
//			List<AppUserFileEntity> fileList = appUserFileDao.findUid(entity.getUid());
//			if (fileList == null || fileList.size() < 1) {
//				return setResultError(I18nUtil.getMessage("user_kyc_null"));
//			}
//			if(!KycStateEnums.SUCCESS_APPROVE.getIndex().equals(userEntity.getKycState())) {
//				return setResultError(I18nUtil.getMessage("user_kyc_null"));
//			}
//			//获取银行卡类型 实体卡/虚拟卡
//			String bankCardNature = cardEntity.getBankCardNature();
//			//余额不足 添加判断
//			if (CardTypeEnums.PHYSICAL.getName().equals(bankCardNature)){
//				//类型
//				AppUserIntiterEntity firstChannelUser = findFirstChannelUser(userEntity.getUid());
//				if (firstChannelUser != null && firstChannelUser.getIntiterUserType().equals(IntiterUserTypeEnums.CHANNEL_USER.getIndex())) {
//					//渠道商的话，直接判断
//					if (!isOpenFee(firstChannelUser)){
//						return setResultError(I18nUtil.getMessage("channel_open_fees"));
//					}
//				}
//			}
//			//判断获取银行卡开卡费用
//			ResponseBase base = this.findOpenCardFee(userEntity.getUid(), entity.getCardUuid());
//			if(HTTP_RES_CODE_200.equals(base.getRtncode())) {
//				cardEntity.setOpenCardCost(Double.parseDouble(base.getData().toString()));
//			}
//
//			// 前置校验用户钱包权限余额是否满足条件
//			if (PayTypeEnums.WALLET.getIndex().equals(entity.getTopupType())) {// 钱包充值
//				// 检查余额
//				if (userEntity == null || userEntity.getWalletBalance() < 0) {
//					return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
//				}
//				if (userEntity != null && userEntity.getWalletBalance() >= 0) {
//					Double monery = userEntity.getWalletBalance();
//					Double temp = monery - (cardEntity.getOpenCardCost() + cardEntity.getPreSaveCost());
//					if (temp < 0) {
//						return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
//					}
//				}
//			}
//
//			//创建申请记录
//			CardApplyEntity applyEntity = this.addApply(entity,cardEntity,cardholderEntity,userEntity);
//			//冻结资产
//			Double money =cardEntity.getMonthFee() + cardEntity.getOpenCardCost() + cardEntity.getPreSaveCost();
//			Double balance = userEntity.getWalletBalance() == null? 0 : userEntity.getWalletBalance();
//			userEntity.setOpenFreezeBalance((userEntity.getOpenFreezeBalance() == null ? 0 : userEntity.getOpenFreezeBalance()) + money);
//			userEntity.setWalletBalance((userEntity.getWalletBalance() == null? 0 : userEntity.getWalletBalance()) - money);
//			appUserDao.updateById(userEntity);
//			//新增钱包冻结记录
//			this.addWalletOpenFreeze(userEntity, money,balance,applyEntity.getId());
//			//判断是否是虚拟卡开卡，且kyc认证已通过，则自动首充激活
//			if(CardTypeEnums.VIRTUAL.getName().equals(applyEntity.getCardType()) && KycStateEnums.SUCCESS_APPROVE.getIndex().equals(applyEntity.getKycState())) {
//				applyServiceImpl.openCard(applyEntity.getId());
//			}
//			//发送通知
//			messageTemplateServiceImpl.unifiedSendAsync(
//					MessageEnums.APPLY_CARD_ZH.getIndex(), 
//					LanguageContext.getLanguage(),
//					userEntity.getUid());
//			
//			//异步发送机器人消息推送
//			this.sendRobotAsync(entity, userEntity, cardEntity);
//			return setResultSuccess(I18nUtil.getMessage("base_success"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//	}

	/**
	 * 比较渠道商所剩余额与开卡余额的大小
	 * @param firstChannelUser
	 * @return
	 */
	public boolean isOpenFee(AppUserIntiterEntity firstChannelUser){
		ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", firstChannelUser.getInviterUserUid()));
		if (channelUserEntity == null){
			AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>().eq("app_uid", firstChannelUser.getInviterUserUid()));
			if (appChannelIntiterEntity != null){
				channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", appChannelIntiterEntity.getChannelUid()));
			}
		}
		ChannelLevelEntity channelLevel = channelLevelDao.selectOne(new QueryWrapper<ChannelLevelEntity>().eq("level_no", channelUserEntity.getLevelNo()));
		//用户channel_level openfee
		Double channelOpenFees = channelLevel.getOpenFees();
		//channel_user 剩余金额
		Double openFee = channelUserEntity.getCardOpeningDeductionFee();
        return channelOpenFees <= openFee;
    }
	
	

	//创建申请记录
	public CardApplyEntity addApply(CardApplyQueryEntity entity,CardEntity cardEntity,AppUserHolderEntity cardholderEntity,AppUserEntity userEntity) {
		try {
			//申请记录保存
			CardApplyEntity applyEntity = new CardApplyEntity();
			applyEntity.setUid(entity.getUid());
			applyEntity.setCardId(entity.getCardId());
			applyEntity.setHolderId(entity.getHolderId());
			applyEntity.setCardType(cardEntity.getBankCardNature());
			applyEntity.setMonthFee(cardEntity.getMonthFee());
			applyEntity.setCardUuid(cardEntity.getUuid());
			applyEntity.setOpenCardCost(cardEntity.getOpenCardCost());
			if(CardTypeEnums.PHYSICAL.getName().equals(cardEntity.getBankCardNature())) {//实体卡没有预存费用
				applyEntity.setPreSaveCost(Constants.ZERO);
			}else {
				applyEntity.setPreSaveCost(cardEntity.getPreSaveCost());
			}
			applyEntity.setTopupType(entity.getTopupType());
			applyEntity.setOpenCardTotal(
					cardEntity.getMonthFee() + cardEntity.getOpenCardCost() + applyEntity.getPreSaveCost());
			applyEntity.setApplyState(OpenCardStateEnums.WAIT_ACTIVATION.getIndex());
			applyEntity.setApplyStateName(OpenCardStateEnums.WAIT_ACTIVATION.getName());
			if(userEntity.getKycState() != null) {
				applyEntity.setKycState(userEntity.getKycState());
			}else {
				applyEntity.setKycState(KycStateEnums.WAIT_APPROVE.getIndex());
			}
			if(userEntity.getKycStateName() != null && userEntity.getKycStateName().length() > 1) {
				applyEntity.setKycStateName(userEntity.getKycStateName());
			}else {
				applyEntity.setKycStateName(KycStateEnums.WAIT_APPROVE.getName());
			}
			if(userEntity.getKycAuditResult() != null) {
				applyEntity.setKycAuditResult(userEntity.getKycAuditResult());					
			}
			if(KycStateEnums.SUCCESS_APPROVE.getIndex().equals(userEntity.getKycState())) {//认证成功，修改申请记录发货状态
				applyEntity.setShippingState(LogisticsStateEnums.WAIT_SUCCESS.getIndex());
				applyEntity.setShippingStateName(LogisticsStateEnums.WAIT_SUCCESS.getName());
			}
			GenericityUtil.setDate(applyEntity);
			cardApplyDao.insert(applyEntity);
			//邮寄地址保存
			if (entity.getMailingAddress() != null) {
				entity.getMailingAddress().setApplyId(applyEntity.getId());
				entity.getMailingAddress().setUid(entity.getUid());
				GenericityUtil.setDate(entity.getMailingAddress());
				cardApplySendDao.insert(entity.getMailingAddress());
			}
			if(entity.getMailingAddressStr() != null && entity.getMailingAddressStr().length() > 0) {
				JSONObject object = JSONObject.parseObject(entity.getMailingAddressStr());
				CardApplySendEntity applySendEntity = JSONObject.parseObject(object.get("mailingAddress").toString(), CardApplySendEntity.class);
				applySendEntity.setApplyId(applyEntity.getId());
				applySendEntity.setUid(entity.getUid());
				GenericityUtil.setDate(applySendEntity);
				cardApplySendDao.insert(applySendEntity);
			}
			//kyc认证信息保存
			if(GenericityUtil.hasContent(entity.getKycData())) {
				entity.getKycData().setApplyId(applyEntity.getId());
				entity.getKycData().setUid(entity.getUid());
				GenericityUtil.setDate(entity.getKycData());
				cardApplyKycDao.insert(entity.getKycData());
			}else if(entity.getKycDataStr() != null && entity.getKycDataStr().length() > 0) {
				JSONObject object = JSONObject.parseObject(entity.getMailingAddressStr());
				CardApplyKycEntity applyKycEntity = JSONObject.parseObject(object.get("kycData").toString(), CardApplyKycEntity.class);
				applyKycEntity.setApplyId(applyEntity.getId());
				applyKycEntity.setUid(entity.getUid());
				GenericityUtil.setDate(applyKycEntity);
				cardApplyKycDao.insert(applyKycEntity);
			}else {
				CardApplyKycEntity kycEntity = cardApplyKycDao.findUid(entity.getUid());
				if(kycEntity != null) {
					if(kycEntity.getApplyId() == null) {
						kycEntity.setApplyId(applyEntity.getId());
						cardApplyKycDao.updateById(kycEntity);	
					}else {
						kycEntity.setApplyId(applyEntity.getId());
						kycEntity.setUid(entity.getUid());
						GenericityUtil.setDate(kycEntity);
						cardApplyKycDao.insert(kycEntity);							
					}
				}
			}
			//申请记录持卡人信息保存
			CardApplyManEntity cardApplyManEntity = new CardApplyManEntity();
			BeanUtils.copyProperties(cardholderEntity, cardApplyManEntity);
			cardApplyManEntity.setId(null);
			cardApplyManEntity.setApplyId(applyEntity.getId());
			GenericityUtil.setDate(cardApplyManEntity);
			cardApplyManDao.insert(cardApplyManEntity);
			return applyEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

//	@Override
//	@Synchronized
//	@SysLogAnnotation(module = "银行卡管理", type = "post", remark = "卡充值")
//	public ResponseBase topUp(RechargeCardEntity entity) {
//		try {
//			entity.setAmount(entity.getTargetAmount());
//			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
//			AppUserCardEntity userCard = appUserCardDao.findUserBankcardIdOrUid(entity.getUserBankcardId(),entity.getUid());
//			if(userEntity != null && userCard != null) {
//				CardEntity cardEntity = cardDao.findByuuId(userCard.getCardUuid());
//				if(cardEntity == null) {
//					return setResultError(I18nUtil.getMessage("bank_card_null"));
//				}
//				if(entity.getType() == null || entity.getType()) {
//					if(userEntity.getPayPassword() != null && userEntity.getPayPassword().length() > 0) {
//						if (!DigestUtils.md5DigestAsHex((entity.getPayPasswrod()).getBytes())
//								.equals(userEntity.getPayPassword())) {
//							return setResultError(I18nUtil.getMessage("pay_password_error"));
//						}
//					}else {
//						return setResult(Constants.HTTP_RES_CODE_602, I18nUtil.getMessage("pay_password_error"), null);
//
//					}
//				}
//				//充值金额限制
//				Double minLimi = cardEntity.getRechargeMinLimit() == null ? 0 : cardEntity.getRechargeMinLimit();
//				if(minLimi > ((entity.getTargetAmount() == null ? 0 : entity.getTargetAmount()) + entity.getHandlingFees())) {
//					return setResultError(I18nUtil.getMessage("topup_money_min"));
//				}
//				Double maxLimit = cardEntity.getRechargeMaxLimit() == null ? 0 : cardEntity.getRechargeMaxLimit();
//				if(maxLimit < ((entity.getTargetAmount() == null ? 0 : entity.getTargetAmount()) + entity.getHandlingFees())) {
//					return setResultError(I18nUtil.getMessage("topup_money_max"));
//				}
//				//未激活无法充值
//				if (!CardStateEnums.ACTIVE.getIndex().toString().equals(userCard.getStatus())) {
//					return setResultError(I18nUtil.getMessage("card_no_active"));
//				}
//				if(PayTypeEnums.WALLET.getIndex().equals(entity.getPayType())) {//钱包充值
//					//检查余额
//					if(userEntity == null || userEntity.getWalletBalance() <= 0) {
//						return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
//					}
//					if(userEntity != null && userEntity.getWalletBalance() > 0) {
//						Double monery = userEntity.getWalletBalance();// - (userEntity.getFreezeBalance() == null ? 0 : userEntity.getFreezeBalance());
//						Double temp = monery - ((entity.getTargetAmount() == null ? 0 : entity.getTargetAmount())
//			                      + (entity.getHandlingFees() == null ? 0 : entity.getHandlingFees()));
//						if(temp < 0) {
//							return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
//						}
//					}
//				}
//				
//				//调用三方充值
//				String orderNum = OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid()));
//				entity.setRequestOrderId(orderNum);
//				ResponseBase base = UCardApi.postData(entity.getUid(), UCardMethods.RECHARGE_BANKCARD, entity, orderNum);
//				if("Recharge processing in progress, We will notify you of the processing results later."
//						.equals(base.getMsg()) 
//						|| "Activation processing in progress, We will notify you of the processing results later."
//						.equals(base.getMsg()) && Constants.HTTP_RES_CODE_500.equals(base.getRtncode())) {
//					//生成预订单卡充值记录
//					CardTransactionEntity transaction = this.addTransaction(userCard, entity, orderNum);
//					cardTransactionDao.insert(transaction);
//					if(PayTypeEnums.WALLET.getIndex().equals(entity.getPayType())) {
//						//生成预订单钱包消费记录
//						WalletLogEntity walletLogEntity = this.addWalletLog(userCard, entity, userEntity, orderNum,WalletTitleTypeEnums.WALLET_TOP_UP.getCnName(),WalletTitleTypeEnums.WALLET_TOP_UP.getIndex());
//						walletLogDao.insert(walletLogEntity);
//						//预减钱包余额
//						Double monery = userEntity.getWalletBalance() == null ? 0 : userEntity.getWalletBalance();
//						Double target = entity.getTargetAmount() == null ? 0 : entity.getTargetAmount();
//						Double fee = entity.getHandlingFees() == null ? 0 : entity.getHandlingFees();
//						Double temp = monery - (target + fee);
//						userEntity.setWalletBalance(temp);
//						appUserDao.updateById(userEntity);
//					}
//					return setResultSuccess(I18nUtil.getMessage("topUp_result"));
//				}
//				return base;
//			}else {
//				return setResultError(I18nUtil.getMessage("base_error"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//	}
	
	
	
	@Synchronized
	public ResponseBase firstTopUp(RechargeCardEntity entity, CardApplyEntity applyData) {
		try {
			entity.setAmount(entity.getTargetAmount());
			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
			AppUserCardEntity userCard = appUserCardDao.findUserBankcardIdOrUid(entity.getUserBankcardId(),entity.getUid());
			if(userEntity != null && userCard != null) {
				CardEntity cardEntity = cardDao.findByuuId(userCard.getCardUuid());
				if(cardEntity == null) {
					return setResultError(I18nUtil.getMessage("bank_card_null"));
				}
				//充值金额限制
				Double minLimi = cardEntity.getActiveMinLimit() == null ? 0 : cardEntity.getActiveMinLimit();
				if(minLimi > entity.getTargetAmount()) {
					return setResultError(I18nUtil.getMessage("topup_money_min"));
				}
				//调用三方充值
				String orderNum = OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid()));
				entity.setRequestOrderId(orderNum);
				System.out.println(JSON.toJSON(entity));
				ResponseBase base = UCardApi.postData(entity.getUid(), UCardMethods.RECHARGE_BANKCARD, entity, orderNum);
				if("Recharge processing in progress, We will notify you of the processing results later."
						.equals(base.getMsg()) 
						|| "Activation processing in progress, We will notify you of the processing results later."
						.equals(base.getMsg()) && Constants.HTTP_RES_CODE_500.equals(base.getRtncode())) {
					//生成预订单卡充值记录
					CardTransactionEntity transaction = this.addTransaction(userCard, entity, orderNum);
					cardTransactionDao.insert(transaction);
					if(PayTypeEnums.WALLET.getIndex().equals(entity.getPayType())) {
						//生成预订单钱包消费记录
						WalletLogEntity walletLogEntity = walletLogDao.findApplyId(applyData.getId().toString());
						if(walletLogEntity != null) {
							walletLogEntity.setOperateType(WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getIndex());
							walletLogEntity.setTitle(WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getCnName());
							walletLogEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
							walletLogEntity.setOutOrderNo(orderNum);
							walletLogEntity.setSetTime(new Date());
							walletLogDao.updateById(walletLogEntity);
						}else {
							WalletLogEntity walletLog = this.addWalletLog(userCard, entity, userEntity, orderNum,WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getCnName(),WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getIndex());
							walletLogDao.insert(walletLog);							
						}
					}
					return setResultSuccess(I18nUtil.getMessage("topUp_result"));
				}
				return base;
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	//生成交易记录
	public CardTransactionEntity addTransaction(AppUserCardEntity userCard,RechargeCardEntity entity,String orderNum) {
		try {
			CardTransactionEntity transaction = new CardTransactionEntity();
			transaction.setUid(userCard.getUid());
			transaction.setUserBankcardId(userCard.getUserBankcardId());
			transaction.setCardId(userCard.getCardId());
			transaction.setCardUuid(userCard.getCardUuid());
			transaction.setCardNo(userCard.getCardNo());
			transaction.setHandlingFees(entity.getHandlingFees());
			transaction.setPayType(entity.getPayType());
			transaction.setRequestOrderId(orderNum);
			transaction.setOrderState(OrderStateEnums.PROCESSING.getName());
			transaction.setLocalCurrency(TransCurrencyEnums.USD.getName());
			transaction.setLocalCurrencyAmt("+"+entity.getTargetAmount());
			transaction.setTransCurrency(TransCurrencyEnums.USD.getName());
			transaction.setTransCurrencyAmt("+"+entity.getTargetAmount());
			transaction.setTransType(TransTypeEnums.TOPUP.getName());
			transaction.setSetUser(entity.getSetUser());
			transaction.setSetUserName(entity.getSetUserName());
			GenericityUtil.setDate(transaction);
			return transaction;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public WalletLogEntity addWalletLog(AppUserCardEntity userCard,RechargeCardEntity entity,AppUserEntity userEntity,String orderNum,String typeName,Integer typeId) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid())));
			walletLogEntity.setOutOrderNo(orderNum);
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
			walletLogEntity.setTitle(typeName);
			walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(entity.getTargetAmount()));
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(entity.getHandlingFees()== null ?0:entity.getHandlingFees()));
			walletLogEntity.setFormName(userEntity.getUserEmail());
			walletLogEntity.setFormAccount(userEntity.getUid());
			walletLogEntity.setToName(userCard.getCardNo());
			walletLogEntity.setToAccount(userCard.getCardNo());
			walletLogEntity.setUserBankcardId(userCard.getUserBankcardId());
			walletLogEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
			walletLogEntity.setOperateType(typeId);
			walletLogEntity.setSetUser(entity.getSetUser());
			walletLogEntity.setSetUserName(entity.getSetUserName());
			GenericityUtil.setDate(walletLogEntity);
			return walletLogEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "get", remark = "实体卡分配")
	public ResponseBase cardBinding(Integer applyId,String cardNumber,String pinNum) {
		try {
			if(pinNum == null || pinNum.length() < 1) {
				return setResultError(I18nUtil.getMessage("pin_null"));
			}
			CardApplyEntity applyEntity = cardApplyDao.selectById(applyId);
			if(applyEntity != null) {
				AppUserEntity userEntity = appUserDao.findByUid(applyEntity.getUid());
				if(userEntity == null) {
					return setResultError(I18nUtil.getMessage("user.account_error"));
				}
				CardEntity cardEntity = cardDao.findByuuId(applyEntity.getCardUuid());
				//校验
				ResponseBase base = this.chekCardBinding(cardEntity, applyEntity, applyId);
				if(!Constants.HTTP_RES_CODE_200.equals(base.getRtncode())){
					return base;
				}
				//查询开卡费
				Double openCardAmount = Constants.ZERO;
				ResponseBase cardBase = this.findOpenCardFee(applyEntity.getUid(), cardEntity.getUuid());
				if (HTTP_RES_CODE_200.equals(cardBase.getRtncode())) {
					openCardAmount = Double.parseDouble(cardBase.getData().toString());
				}
				// 检查余额
				if (userEntity == null || userEntity.getWalletBalance() < 0) {
					return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
				}
				if (userEntity != null && userEntity.getWalletBalance() >= 0) {
					Double monery = userEntity.getWalletBalance();// 钱包余额
					Double temp = monery -openCardAmount;
					if (temp < 0) {
						return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
					}
				}
				//绑定激活
				ResponseBase responseBase = this.activaEntityCard(applyEntity,cardNumber,cardEntity,pinNum);
				if(Constants.HTTP_RES_CODE_200.equals(responseBase.getRtncode())) {
					//修改申请记录状态
					applyEntity.setApplyState(OpenCardStateEnums.PROCESS_ACTIVATION.getIndex());
					applyEntity.setApplyStateName(OpenCardStateEnums.PROCESS_ACTIVATION.getName());
					cardApplyDao.updateById(applyEntity);
					//减余额(开卡费)
					userEntity.setWalletBalance(userEntity.getWalletBalance() - openCardAmount);
					appUserDao.updateById(userEntity);
					AppUserCardEntity appUserCardEntity = JSONObject.parseObject(JSON.toJSONString(responseBase.getData()), AppUserCardEntity.class);
					this.addOpenCardEntityWalletLog(userEntity,
							appUserCardEntity,
							WalletTitleTypeEnums.OPEN_CARD.getCnName(),
							WalletTitleTypeEnums.OPEN_CARD.getIndex(),
							openCardAmount);
					
					//发送通知
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.CARD_ASSIGN_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							applyEntity.getUid(), 
							cardNumber);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}
				return responseBase;
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category 新增实体卡开卡记录
	 * @param userEntity
	 * @param appUserCardEntity
	 * @param typeName
	 * @param typeId
	 * @param openCardAmount
	 * @return
	 */
	public WalletLogEntity addOpenCardEntityWalletLog(AppUserEntity userEntity, AppUserCardEntity appUserCardEntity, String typeName, Integer typeId, Double openCardAmount) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			String orderNum = OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid()));
			walletLogEntity.setOrderNo(orderNum);
			walletLogEntity.setOutOrderNo(orderNum);
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
			walletLogEntity.setTitle(typeName);
			walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(openCardAmount));
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(0.0));
			walletLogEntity.setFormName(userEntity.getUserEmail());
			walletLogEntity.setFormAccount(userEntity.getUid());
			walletLogEntity.setToName(appUserCardEntity.getCardNo());
			walletLogEntity.setToAccount(appUserCardEntity.getCardNo());
			walletLogEntity.setUserBankcardId(appUserCardEntity.getUserBankcardId());
			walletLogEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
			walletLogEntity.setOperateType(typeId);
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
			//新增开卡记录
			AppUserCardOpenEntity appUserCardOpenEntity = new AppUserCardOpenEntity();
			appUserCardOpenEntity.setOpenCardAmount(openCardAmount);
			appUserCardOpenEntity.setOpenState(PublicEnums.ONE.getIndex());
			appUserCardOpenEntity.setOrderNum(orderNum);
			appUserCardOpenEntity.setUid(userEntity.getUid());
			appUserCardOpenEntity.setUserBankcardId(appUserCardEntity.getUserBankcardId());
			GenericityUtil.setDate(appUserCardOpenEntity);
			appUserCardOpenDao.insert(appUserCardOpenEntity);
			return walletLogEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	
	
	/**
	 * @category 实体卡绑定前置校验
	 * @param cardEntity
	 * @param applyEntity
	 * @param applyId
	 * @return
	 */
	public ResponseBase chekCardBinding(CardEntity cardEntity,CardApplyEntity applyEntity,Integer applyId) {
		if(cardEntity == null) {
			return setResultError(I18nUtil.getMessage("bank_card_null"));
		}
		if(!CardTypeEnums.PHYSICAL.getName().equals(applyEntity.getCardType())) {
			return setResultError(I18nUtil.getMessage("physical_card_no"));
		}
		if(!KycStateEnums.SUCCESS_APPROVE.getIndex().equals(applyEntity.getKycState())) {
			return setResultError(I18nUtil.getMessage("kyc_audit_result"));
		}
		if(OpenCardStateEnums.SUCCESS_ACTIVATION.getIndex().equals(applyEntity.getApplyState())) {
			return setResultError(I18nUtil.getMessage("bank_card_binding"));
		}
		CardApplySendEntity applySendEntity = cardApplySendDao.findByApplyId(applyId);
		if(applySendEntity == null) {
			return setResultError(I18nUtil.getMessage("mailingAddress_null"));
		}
//		if(!LogisticsStateEnums.ALREADY_SHIPPING.getIndex().equals(applyEntity.getShippingState())) {
//			return setResultError(I18nUtil.getMessage("shipping_state_error"));
//		}
		return setResultSuccess();
	}
	
	/**
	 * @category 实体卡激活
	 * @param applyEntity
	 * @param cardNumber
	 * @param cardInfo
	 * @param pinNum
	 * @return
	 */
	public ResponseBase activaEntityCard(CardApplyEntity applyEntity, String cardNumber, CardEntity cardInfo, String pinNum) {
		try {
			CardApplyManEntity applyManEntity = cardApplyManDao.findByApplyId(applyEntity.getId());
			CardApplySendEntity applySendEntity = cardApplySendDao.findByApplyId(applyEntity.getId());
			if(applyManEntity != null && applySendEntity != null) {
				ActivaCardEntity cardEntity = new ActivaCardEntity();
				cardEntity.setAddress(applySendEntity.getAddressInfo());
				cardEntity.setCardNo(cardNumber);
				if(applySendEntity.getCity() == null || applySendEntity.getCity().isEmpty()) {
					cardEntity.setCity(applySendEntity.getProvince());
				}else {
					cardEntity.setCity(applySendEntity.getCity());					
				}
				cardEntity.setCountryCode(applyManEntity.getUserTelCode());
				cardEntity.setMobile(applyManEntity.getUserTel());
				cardEntity.setMobilePrefix(applyManEntity.getUserTelDialCode());
				cardEntity.setPostCode(applySendEntity.getPostCode());
				cardEntity.setProductId(applyEntity.getCardId());
				cardEntity.setState(applySendEntity.getProvince());
				ResponseBase base = UCardApi.postData(applyEntity.getUid(), UCardMethods.BANKCARD_ACTIVE, cardEntity,null);
				if(Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
					AssignResEntity assignResEntity = JSONObject.parseObject(base.getData().toString(), AssignResEntity.class);
					AppUserCardEntity userCardEntity = new AppUserCardEntity();
					userCardEntity.setApplyId(applyEntity.getId());
					userCardEntity.setBalance(0.0);
					userCardEntity.setCardId(applyEntity.getCardId());
					userCardEntity.setCardNo(cardNumber);
					userCardEntity.setCardType(applyEntity.getCardType());
					userCardEntity.setStatus(CardStateEnums.ACTIVE_PROCESSING.getIndex().toString());
					userCardEntity.setUid(applyEntity.getUid());
					userCardEntity.setUserBankcardId(Integer.parseInt(assignResEntity.getUserBankcardId()));
					userCardEntity.setCardUuid(cardInfo.getUuid());
					String aesPin = AesAPIUtils.encrypt(pinNum, APPS_ECRET);
					userCardEntity.setPinNum(aesPin);
					GenericityUtil.setDate(userCardEntity);
					appUserCardDao.insert(userCardEntity);
					return setResultSuccess(userCardEntity, Constants.SUCCESS);
				}
				return base;
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return setResultError(Constants.ERROR);
		}
	}


	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "get", remark = "实体卡发货")
	public ResponseBase shipping(CardShippingEntity entity,HttpServletRequest request) {
		try {
			if(entity.getLogisticsMonery() == null) {
				entity.setLogisticsMonery(0.0);
			}
			CardApplyEntity applyEntity = cardApplyDao.selectById(entity.getApplyId());
			if(applyEntity != null) {
				AppUserEntity userData = appUserDao.findByUid(applyEntity.getUid());
				if(userData == null) {
					return setResultError(I18nUtil.getMessage("account_error"));
				}
				if(!LogisticsStateEnums.WAIT_SUCCESS.getIndex().equals(applyEntity.getShippingState())) {
					return setResultError(I18nUtil.getMessage("shipping_already"));
				}
				
				if(this.isValidTrackingNumber(entity.getLogisticsNum())) {
					//三方物流单号注册
					TrackingEntity trackingEntity = new TrackingEntity();
					trackingEntity.setNumber(entity.getLogisticsNum());
					trackingEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(applyEntity.getUid())));
					List<TrackingEntity> list = new ArrayList<>();
					list.add(trackingEntity);
					String jsonParam = JSON.toJSONString(list);
					String res = HttpUtil.emsPost(EmsUrlEnums.REGISTER_NUMBER.getName(), jsonParam,EMS_API_KEY);
					if(res == null || res.length() < 1) {
						return setResultError(I18nUtil.getMessage("base_error"));
					}
					//添加物流记录
					entity.setLogisticsState(LogisticsStateEnums.ALREADY_SHIPPING.getIndex());
					entity.setLogisticsStateName(LogisticsStateEnums.ALREADY_SHIPPING.getName());
					entity.setUid(applyEntity.getUid());
					entity.setOperateUserIp(ipUtil.getClientIp(request));
					CardApplyManEntity manEntity = cardApplyManDao.findByApplyId(entity.getApplyId());
					if(manEntity != null) {
						entity.setUserEmail(manEntity.getUserEmail());
						entity.setUserName(manEntity.getUserName());
						entity.setUserTel(manEntity.getUserTel());
					}
					GenericityUtil.setDate(entity);
					cardShippingDao.insert(entity);
					//修改申请记录发货状态
					applyEntity.setShippingState(LogisticsStateEnums.ALREADY_SHIPPING.getIndex());
					applyEntity.setShippingStateName(LogisticsStateEnums.ALREADY_SHIPPING.getName());
					applyEntity.setShippingTime(new Date());
					applyEntity.setLogisticsNum(entity.getLogisticsNum());
					applyEntity.setLogisticsMonery(entity.getLogisticsMonery());
					cardApplyDao.updateById(applyEntity);
					//扣除钱包余额
					if(PayTypeEnums.WALLET.getIndex().equals(applyEntity.getTopupType())) {
						//生成预订单钱包消费记录
						WalletLogEntity walletLog = this.addWalletList(entity, userData,applyEntity);
						walletLogDao.insert(walletLog);		
						//减钱包余额（减运费）
						Double monery = userData.getWalletBalance() == null ? 0 : userData.getWalletBalance();
						Double fee = entity.getLogisticsMonery() == null ? 0 : entity.getLogisticsMonery();
						userData.setWalletBalance(monery - fee);
						appUserDao.updateById(userData);
						BigDecimal realMoney = walletLog.getRealMoney() != null ? walletLog.getRealMoney() : BigDecimal.ZERO;
						BigDecimal serviceCharge = walletLog.getServiceCharge() != null ? walletLog.getServiceCharge() : BigDecimal.ZERO;						
						//发送通知
						messageTemplateServiceImpl.unifiedSendAsync(
								MessageEnums.WALLET_ZH.getIndex(), 
								LanguageContext.getLanguage(),
								applyEntity.getUid(), 
								"-"+(realMoney.add(serviceCharge))+TransCurrencyEnums.USD.getName(),
								userData.getWalletBalance()+TransCurrencyEnums.USD.getName(),
								userData.getFreezeBalance()+TransCurrencyEnums.USD.getName());
						
					}
			
					//发送通知
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.CARD_SHIPPING_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							entity.getUid(), 
							entity.getLogisticsNum());
					
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}else {
					return setResultError(I18nUtil.getMessage("logistics_error"));					
				}
				
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	public WalletLogEntity addWalletList(CardShippingEntity entity, AppUserEntity userData, CardApplyEntity applyEntity) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userData.getUid())));
			walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userData.getUid())));
			walletLogEntity.setUserId(userData.getId());
			walletLogEntity.setUid(userData.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
			walletLogEntity.setTitle(WalletTitleTypeEnums.ENTITY_CARD_FREIGHT.getCnName());
			walletLogEntity.setPrimevalMoney(userData.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(entity.getLogisticsMonery()+applyEntity.getOpenCardTotal()));
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));
			walletLogEntity.setFormName(WalletTitleTypeEnums.ENTITY_CARD_FREIGHT.getCnName());
			walletLogEntity.setFormAccount(WalletTitleTypeEnums.ENTITY_CARD_FREIGHT.getCnName());
			walletLogEntity.setToName(userData.getUserEmail());
			walletLogEntity.setToAccount(userData.getUserEmail());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.OPEN_CARD.getIndex());
			walletLogEntity.setSetUser(entity.getOperateUserId());
			walletLogEntity.setSetUserName(entity.getOperateUserName());
			GenericityUtil.setDate(walletLogEntity);
			return walletLogEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "get", remark = "修改物流单号")
	public ResponseBase upLogisticsNum(Integer applyId, String logisticsNum) {
		try {
			CardApplyEntity applyEntity = cardApplyDao.selectById(applyId);
			if(applyEntity != null) {
				AppUserCardEntity userCardEntity = appUserCardDao.findApplyId(applyId);
				if(userCardEntity == null) {
					return setResultError(I18nUtil.getMessage("bank_not_binding"));
				}
				if(userCardEntity.getLogisticsNum() == null || userCardEntity.getLogisticsNum().length() <1) {
					return setResultError(I18nUtil.getMessage("shipping_not"));
				}
				if(this.isValidTrackingNumber(logisticsNum)) {
					//三方物流单号注册
					TrackingEntity trackingEntity = new TrackingEntity();
					trackingEntity.setNumber(logisticsNum);
					trackingEntity.setOrderNo(userCardEntity.getCardNo());
					List<TrackingEntity> list = new ArrayList<>();
					list.add(trackingEntity);
					String jsonParam = JSON.toJSONString(list);
					String res = HttpUtil.emsPost(EmsUrlEnums.REGISTER_NUMBER.getName(), jsonParam,EMS_API_KEY);
					if(res == null || res.length() < 1) {
						return setResultError(I18nUtil.getMessage("base_error"));
					}
					TrackingRegisterEntity registerEntity = JSONObject.parseObject(res, TrackingRegisterEntity.class);
					if(registerEntity.getData().getRejected() != null && registerEntity.getData().getRejected().size() > 0) {
						return setResultError(registerEntity.getData().getRejected().get(0).getError().getMessage());
					}
					userCardEntity.setShippingState(LogisticsStateEnums.ALREADY_SHIPPING.getIndex());
					userCardEntity.setShippingStateName(LogisticsStateEnums.ALREADY_SHIPPING.getName());
					userCardEntity.setShippingTime(new Date());
					userCardEntity.setLogisticsNum(logisticsNum);
					userCardEntity.setGmtModified(new Date());
					appUserCardDao.updateById(userCardEntity);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}else {
					return setResultError(I18nUtil.getMessage("logistics_error"));					
				}
				
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	/**
     * 校验物流单号是否是合法格式
     * 规则：必须是连续的 5 到 50 个字母、数字和中杠组成的字符串
     * @param trackingNumber 要校验的物流单号
     * @return 合法返回 true，非法返回 false
     */
    public boolean isValidTrackingNumber(String trackingNumber) {
        if (trackingNumber == null) {
            return false;
        }
        // 正则表达式：只允许大小写字母、数字和中杠，长度5~50
        String regex = "^[A-Za-z0-9-]{5,50}$";
        return trackingNumber.matches(regex);
    }

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "get", remark = "查询物流单跟踪记录")
	public ResponseBase findLogistics(String logisticsNum,Integer applyId) {
		try {
			if(logisticsNum == null || logisticsNum.length() < 1) {
				return setResultError(I18nUtil.getMessage("logistics_not_binding"));
			}
			List<CardApplyEntity> applyList = cardApplyDao.findLogisticsNum(logisticsNum);
			Integer shippingState = null;
			String ShippingStateName = null;
			if(applyList != null && applyList.size() > 0) {
				CardApplyEntity cardApplyEntity = new CardApplyEntity();
				cardApplyEntity.setLogisticsNum(logisticsNum);
				if(applyId != null) {
					CardApplyEntity applyEntity = cardApplyDao.selectById(applyId);
					if(applyEntity != null) {
						 BeanUtils.copyProperties(applyEntity, cardApplyEntity);
					}
				}
				cardApplyEntity.setLogisticsNum(logisticsNum); // 保留新值
				CardEntity cardEntity = cardDao.findByuuId(cardApplyEntity.getCardUuid());
				if(cardEntity != null) {
					cardApplyEntity.setCardData(cardEntity);
				}
				AppUserCardEntity appUserCardEntity = appUserCardDao.findApplyId(applyId);
				if(appUserCardEntity != null) {
					cardApplyEntity.setCardNo(appUserCardEntity.getCardNo());
				}
				//根据物流单号获取物流信息
				TrackingEntity trackingEntity = new TrackingEntity();
				trackingEntity.setNumber(logisticsNum);
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
						shippingState = LogisticsStateEnums.INTRANSIT.getIndex();
						ShippingStateName = LogisticsStateEnums.INTRANSIT.getName();
					}else if(EmsStateEnums.OUTFORDELIVERY.getName().equals(state)) {//配送中
						shippingState = LogisticsStateEnums.OUTFORDELIVERY.getIndex();
						ShippingStateName = LogisticsStateEnums.OUTFORDELIVERY.getName();
					}else if(EmsStateEnums.DELIVERED.getName().equals(state)) {//签收
						shippingState = LogisticsStateEnums.DELIVERED.getIndex();
						ShippingStateName = LogisticsStateEnums.DELIVERED.getName();
					}
					Tracking tracking = temp.getTrack_info().getTracking();
					if(tracking != null) {
						List<Provider> providers = tracking.getProviders();
						if(providers != null && providers.size() > 0) {
							Provider provider = providers.get(0);
							if(provider != null) {
								List<Event> eventList = provider.getEvents();
								List<Event> newList = this.withSubState(eventList, applyList.get(0));
//								entity.setLogisticsInfo(newList);
								cardApplyEntity.setLogisticsInfo(newList);
							}
						}
					}
				}
				//修改物流状态
				for (int i = 0; i < applyList.size(); i++) {
					applyList.get(i).setShippingState(shippingState);
					applyList.get(i).setShippingStateName(ShippingStateName);
					cardApplyDao.updateById(applyList.get(i));
					CardShippingEntity shippingEntity = cardShippingDao.findByApplyId(applyList.get(i).getId());
					if(shippingEntity != null) {
						shippingEntity.setLogisticsState(shippingState);
						shippingEntity.setLogisticsStateName(ShippingStateName);
						cardShippingDao.updateById(shippingEntity);
					}
				}
				cardApplyEntity.setShippingState(shippingState);
				cardApplyEntity.setShippingStateName(ShippingStateName);
				return setResultSuccess(cardApplyEntity, I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 订单物流节点状态处理
	public List<Event> withSubState(List<Event> list, CardApplyEntity entity) {
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
	@SysLogAnnotation(module = "银行卡管理", type = "post", remark = "银行卡信息分页列表")
	public ResponseBase findListPag(CardEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			entity.setLanguage(LanguageContext.getLanguage());
			List<CardEntity> list = cardDao.findList(entity);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					// 标签列表
					List<Integer> lableList = cardLableDao.queryByCardId(list.get(i).getUuid());
					list.get(i).setLableIdList(lableList);
					// 卡片简介
					List<Integer> synopsisList = cardSynopsisDao.queryByCardId(list.get(i).getUuid());
					list.get(i).setSynopsisIdList(synopsisList);
				}
			}
			PageInfo<CardEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡管理", type = "post", remark = "修改封面图")
	public ResponseBase updateImg(String uuid, MultipartFile file) {
		try {
			CardEntity cardEntity = cardDao.findByuuId(uuid);
			if (cardEntity != null) {
				if (file != null) {
					String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.CARD.getName());
					if (fileUrl != null && fileUrl.length() > 0) {
						cardEntity.setImg(fileUrl);
					}
				}
				cardDao.updateByImage(cardEntity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	public ResponseBase updateListImg(String uuid, MultipartFile file) {
		try {
			CardEntity cardEntity = cardDao.findByuuId(uuid);
			if (cardEntity != null) {
				if (file != null) {
					String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.CARD.getName());
					if (fileUrl != null && fileUrl.length() > 0) {
						cardEntity.setListImg(fileUrl);
					}
				}
				cardDao.updateByImageList(cardEntity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase copyCard(String uuid) {
		try {
			CardEntity entity = cardDao.findByuuId(uuid);
			if (entity != null) {
				entity.setUuid(UUID.randomUUID().toString().replace("-", ""));
				GenericityUtil.setDate(entity);
				cardDao.insert(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase bankTopUpSend(String uid, Integer type) {
		try {
			String language = LanguageContext.getLanguage();
			AppUserEntity userEntity = appUserDao.findByUid(uid);
//				AppUserCardEntity userCard = appUserCardDao.findUserBankcardIdOrUid(entity.getUserBankcardId(),entity.getUid());
			if (userEntity != null) {
				if (PublicEnums.ONE.getIndex().equals(type)) {
					EmailTemplateEntity templateEntity = emailTemplateDao
							.findByNum(MessageEnums.ROBOT_TOP_UP_ZH.getIndex(), language);
					if (templateEntity != null) {
						String htmlContent = MessageFormatUtils.format(templateEntity.getTemplateContent(),
								userEntity.getUserEmail(), uid);
						RobotNotifyEntity notifyEntity = new RobotNotifyEntity();
						notifyEntity.setType(RobotRoleEnums.CUSTOMER_SERVICE.getIndex());
						notifyEntity.setContent(htmlContent);
						WalletApi.walletPost(UsdtTopinMethods.MSG_NOTIFY, notifyEntity);
						return setResultSuccess(I18nUtil.getMessage("base_success"));
					}
				} else {
					EmailTemplateEntity templateEntity = emailTemplateDao
							.findByNum(MessageEnums.ROBOT_TOP_UP_WALLET_ZH.getIndex(), language);
					if (templateEntity != null) {
						String htmlContent = MessageFormatUtils.format(templateEntity.getTemplateContent(),
								userEntity.getUserEmail(), uid);
						RobotNotifyEntity notifyEntity = new RobotNotifyEntity();
						notifyEntity.setType(RobotRoleEnums.CUSTOMER_SERVICE.getIndex());
						notifyEntity.setContent(htmlContent);
						WalletApi.walletPost(UsdtTopinMethods.MSG_NOTIFY, notifyEntity);
						return setResultSuccess(I18nUtil.getMessage("base_success"));
					}
				}
				return setResultError(I18nUtil.getMessage("base_error"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 发送机器人消息
	public void sendRobot(CardApplyQueryEntity entity, AppUserEntity userEntity, CardEntity cardEntity) {
		if (PayTypeEnums.WALLET.getIndex().equals(entity.getTopupType())) {// 钱包开卡申请
			EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(MessageEnums.ROBOT_OPEN_CARD_ZH.getIndex(),
					LanguageContext.getLanguage());
			if (templateEntity != null) {
				String htmlContent = MessageFormatUtils.format(templateEntity.getTemplateContent(),
						userEntity.getUserEmail(), cardEntity.getBankCardNature());
				RobotNotifyEntity notifyEntity = new RobotNotifyEntity();
				notifyEntity.setType(RobotRoleEnums.CUSTOMER_SERVICE.getIndex());
				notifyEntity.setContent(htmlContent);
				WalletApi.walletPost(UsdtTopinMethods.MSG_NOTIFY, notifyEntity);
			}
		} else {// 银行卡开卡申请
			EmailTemplateEntity templateEntity = emailTemplateDao
					.findByNum(MessageEnums.ROBOT_OPEN_CARD_BANK_ZH.getIndex(), LanguageContext.getLanguage());
			if (templateEntity != null) {
				String htmlContent = MessageFormatUtils.format(templateEntity.getTemplateContent(),
						userEntity.getUserEmail(), cardEntity.getBankCardNature());
				RobotNotifyEntity notifyEntity = new RobotNotifyEntity();
				notifyEntity.setType(RobotRoleEnums.FINANCE.getIndex());
				notifyEntity.setContent(htmlContent);
				WalletApi.walletPost(UsdtTopinMethods.MSG_NOTIFY, notifyEntity);
			}
		}
	}

	@Async("asyncExecutor") // 指定线程池，可省略参数
	public void sendRobotAsync(CardApplyQueryEntity entity, AppUserEntity userEntity, CardEntity cardEntity) {
		try {
			// 调用原有的同步方法
			this.sendRobot(entity, userEntity, cardEntity);
		} catch (Exception e) {
			// 捕获异常，防止线程异常终止
			e.printStackTrace();
		}
	}

	@Override
	public ResponseBase setPin(String uid, Integer userBankcardId, String pinNum) {
		try {
			AppUserEntity appUser = appUserDao.findByUid(uid);
			AppUserCardEntity userCard = appUserCardDao.findUserBankcardId(userBankcardId);
			if(appUser != null && userCard != null) {
				SetPinEntity entity = new SetPinEntity();
				entity.setPin(pinNum);
				entity.setUserBankcardId(userBankcardId);
				ResponseBase base = UCardApi.postData(uid, UCardMethods.SET_BANKCARD_PIN, entity, null);
				if(HTTP_RES_CODE_200.equals(base.getRtncode())) {
					userCard.setPinNum(DigestUtils.md5DigestAsHex((pinNum).getBytes()));
					appUserCardDao.updateById(userCard);
				}
				return base;
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
//	@Async("asyncExecutor") // 指定线程池，可省略参数
//	public void setPinAsync(String uid, Integer userBankcardId, String pinNum) {
//	    try {
//	    	 retrySetPin(uid, userBankcardId, pinNum, 10, 5000L); // 最大重试10次，间隔5秒
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
	
	@Async("asyncExecutor") // 你可以指定线程池
	public CompletableFuture<Void> setPinAsync(String uid, Integer userBankcardId, String pinNum) {
	    return CompletableFuture.runAsync(() -> {
	        try {
	            retrySetPin(uid, userBankcardId, pinNum, 10, 5000L); // 最大重试10次，间隔5秒
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}
	
	 /**
     * 异步处理 setPin，带重试机制
     */
    private void retrySetPin(String uid, Integer userBankcardId, String pinNum, int maxRetry, long interval) {
        int retryCount = 0;
        while (retryCount < maxRetry) {
            try {
                // 查询三方状态
                boolean cardState = this.checkCardStatus(uid, userBankcardId);
                
                if (cardState) {
                    // 如果三方状态正常，执行 setPin
                    ResponseBase response = setPin(uid, userBankcardId, pinNum);
                    if (HTTP_RES_CODE_200.equals(response.getRtncode())) {
                        // 成功，跳出重试
                        break;
                    } else {
                        // 如果状态不为200，重试
                        retryCount++;
                        Thread.sleep(interval);
                    }
                } else {
                    // 三方状态未就绪，重试
                	
                    retryCount++;
                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                e.printStackTrace();
                retryCount++;
                try {
                    Thread.sleep(interval); // 重试间隔
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        if (retryCount >= maxRetry) {
            // 超过最大重试次数，记录日志或处理异常
            System.err.println("setPin 异步执行失败，超过最大重试次数 uid=" + uid);
        }
    }
    
    public boolean checkCardStatus(String uid, Integer userBankcardId){
    	UserBankcardEntity bankcardEntity = new UserBankcardEntity();
		bankcardEntity.setUserBankcardId(userBankcardId);
		ResponseBase base = UCardApi.postData(uid, UCardMethods.BANKCARD_INFO, bankcardEntity, null);
		if(HTTP_RES_CODE_200.equals(base.getRtncode())) {
			CardDataResEntity entity = JSONObject.parseObject(base.getData().toString(), CardDataResEntity.class);
			if(CardStateEnums.ACTIVE.getName().equals(entity.getUserBankCardStatus())) {
				return true;
			}
		}
		return false;
    }
    
    public WalletLogEntity addWalletOpenFreeze(AppUserEntity userEntity, Double money, Double balance, Integer applyId) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(applyId.toString());
			walletLogEntity.setOutOrderNo(applyId.toString());
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
			walletLogEntity.setTitle(WalletTitleTypeEnums.OPEN_CARD_FREEZE.getCnName());
			walletLogEntity.setPrimevalMoney(balance);
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(money));
			walletLogEntity.setFormName(userEntity.getUserEmail());
			walletLogEntity.setFormAccount(userEntity.getUserEmail());
			walletLogEntity.setToName(userEntity.getUserEmail());
			walletLogEntity.setToAccount(userEntity.getUserEmail());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.OPEN_CARD_FREEZE.getIndex());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
			return walletLogEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findOpenCardFee(String uid, String cardUuid) {
		try {
			AppUserEntity appUser = appUserDao.findByUid(uid);
			if(appUser == null) {
				return setResultError(I18nUtil.getMessage("user.not_null"));
			}
			CardEntity cardEntity = cardDao.findByuuId(cardUuid);
			if(cardEntity == null) {
				return setResultError(I18nUtil.getMessage("bank_card_null"));
			}
			//递归查询三级是否有渠道商
			AppUserIntiterEntity userIntiterEntity =  this.findFirstChannelUser(uid);
			if(userIntiterEntity != null) {
				ChannelCardEntity channelCardEntity = channelCardDao.findOne(userIntiterEntity.getInviterUser(),cardUuid);
				if(channelCardEntity != null) {
					return setResultSuccess(channelCardEntity.getOpenFee(), I18nUtil.getMessage("base_success"));
				}
			}
			//如果没有渠道商，查询默认银行卡配置
			CardEntity entity = cardDao.findByuuId(cardUuid);
			if(entity != null) {
				return setResultSuccess(entity.getOpenCardCost(), I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("channel_card_null"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public AppUserIntiterEntity findFirstChannelUser(String startUid) {
	    return findChannelUserRecursive(startUid, 1);
	}

	
	private AppUserIntiterEntity findChannelUserRecursive(String currentUid, int currentLevel) {
	    // 终止条件1：超过3级 取消三级查询限制
//	    if (currentLevel > 3) {
//	        return null;
//	    }
	    // 查询当前用户
	    AppUserIntiterEntity userIntiterEntity = appUserIntiterDao.findByUidCoverInviterUser(currentUid);
	    if (userIntiterEntity == null) {
	        return null;
	    }
	    // 判断是否是CHANNEL_USER
	    if (IntiterUserTypeEnums.CHANNEL_USER.getIndex().equals(userIntiterEntity.getIntiterUserType())) {
	        // 找到CHANNEL_USER，直接返回
	        return userIntiterEntity;
	    }
	    // 获取邀请人UID，继续往上查找
	    String inviterUid = userIntiterEntity.getInviterUserUid();
	    if (StringUtils.isBlank(inviterUid)) {
	        return null; // 没有邀请人，结束
	    }
	    // 递归查找邀请人
	    return findChannelUserRecursive(inviterUid, currentLevel + 1);
	}

	@Override
	@Synchronized
	public ResponseBase openCardApplyV2(CardApplyQueryEntity entity) {
		try {
			// 获取当前app用户
			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
			if (userEntity == null) {
				return setResultError(I18nUtil.getMessage("user.account_error"));
			}
			// 获取申请银行卡信息
			CardEntity cardEntity = cardDao.findByuuId(entity.getCardUuid());
			if (cardEntity == null) {
				return setResultError(I18nUtil.getMessage("bank_card_null"));
			}
			//kyc认证信息
			List<AppUserFileEntity> fileList = appUserFileDao.findUid(entity.getUid());
			if (fileList == null || fileList.size() < 1) {
				return setResultError(I18nUtil.getMessage("user_kyc_null"));
			}
			if(!KycStateEnums.SUCCESS_APPROVE.getIndex().equals(userEntity.getKycState())) {
				return setResultError(I18nUtil.getMessage("user_kyc_null"));
			}
			AppUserHolderEntity holderEntity = null;
			if(entity.getHolderId() != null) {
				holderEntity = appUserHolderDao.selectById(entity.getHolderId());
			}else {
				//新增持卡人信息
				if(entity.getAppUserHolderData() != null) {
					entity.getAppUserHolderData().setUid(entity.getUid());
					GenericityUtil.setDate(entity.getAppUserHolderData());
					ResponseBase base = cardholderServiceImpl.add(entity.getAppUserHolderData());
					if (!base.getRtncode().equals(HTTP_RES_CODE_200)){
						return base;
					}
					String dataStr = JSON.toJSONString(base.getData());
					holderEntity = JSONObject.parseObject(dataStr,AppUserHolderEntity.class);
				}else if(entity.getAppUserHolderStr()!= null && entity.getAppUserHolderStr().length() > 0) {
					JSONObject object = JSONObject.parseObject(entity.getAppUserHolderStr());
					AppUserHolderEntity appUserHolderEntity = JSONObject.parseObject(object.get("appUserHolderData").toString(), AppUserHolderEntity.class);
					appUserHolderEntity.setUid(entity.getUid());
					GenericityUtil.setDate(appUserHolderEntity);
					ResponseBase base = cardholderServiceImpl.add(appUserHolderEntity);
					if (!base.getRtncode().equals(HTTP_RES_CODE_200)){
						return base;
					}
					String dataStr = JSON.toJSONString(base.getData());
					holderEntity = JSONObject.parseObject(dataStr,AppUserHolderEntity.class);
				}
				entity.setHolderId(holderEntity.getId());
			}
			//创建申请记录
			CardApplyEntity applyEntity = this.addApplyV2(entity,cardEntity,holderEntity,userEntity);
			//判断是否是虚拟卡开卡，且kyc认证已通过，则自动开卡
			if(CardTypeEnums.VIRTUAL.getName().equals(cardEntity.getBankCardNature()) && KycStateEnums.SUCCESS_APPROVE.getIndex().equals(userEntity.getKycState())) {
				System.out.println("*********************进行自动开卡**********************");
				ResponseBase base = applyServiceImpl.openCardV2(applyEntity.getId());
				if(Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
					//发送通知
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.APPLY_CARD_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							userEntity.getUid());
					
					//异步发送机器人消息推送
//					this.sendRobotAsync(entity, userEntity, cardEntity);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}else {
					return base;
				}
			}
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	//创建申请记录
		public CardApplyEntity addApplyV2(CardApplyQueryEntity entity,CardEntity cardEntity,AppUserHolderEntity cardholderEntity,AppUserEntity userEntity) {
			try {
				//申请记录保存
				CardApplyEntity applyEntity = new CardApplyEntity();
				applyEntity.setPortType(PublicEnums.ONE.getIndex());
				applyEntity.setUid(entity.getUid());
				applyEntity.setCardId(entity.getCardId());
				applyEntity.setHolderId(entity.getHolderId());
				applyEntity.setCardType(cardEntity.getBankCardNature());
				applyEntity.setCardUuid(cardEntity.getUuid());
				applyEntity.setMonthFee(Constants.ZERO_INT);
				applyEntity.setOpenCardCost(Constants.ZERO);
				applyEntity.setPreSaveCost(Constants.ZERO);
				applyEntity.setTopupType(entity.getTopupType());
				applyEntity.setOpenCardTotal(Constants.ZERO);
				applyEntity.setApplyState(OpenCardStateEnums.WAIT_ACTIVATION.getIndex());
				applyEntity.setApplyStateName(OpenCardStateEnums.WAIT_ACTIVATION.getName());
				if(userEntity.getKycState() != null) {
					applyEntity.setKycState(userEntity.getKycState());
				}else {
					applyEntity.setKycState(KycStateEnums.WAIT_APPROVE.getIndex());
				}
				if(userEntity.getKycStateName() != null && userEntity.getKycStateName().length() > 1) {
					applyEntity.setKycStateName(userEntity.getKycStateName());
				}else {
					applyEntity.setKycStateName(KycStateEnums.WAIT_APPROVE.getName());
				}
				if(userEntity.getKycAuditResult() != null) {
					applyEntity.setKycAuditResult(userEntity.getKycAuditResult());					
				}
				if(KycStateEnums.SUCCESS_APPROVE.getIndex().equals(userEntity.getKycState())) {//认证成功，修改申请记录发货状态
					applyEntity.setShippingState(LogisticsStateEnums.WAIT_SUCCESS.getIndex());
					applyEntity.setShippingStateName(LogisticsStateEnums.WAIT_SUCCESS.getName());
				}
				GenericityUtil.setDate(applyEntity);
				cardApplyDao.insert(applyEntity);
				//邮寄地址保存
				if (entity.getMailingAddress() != null) {
					entity.getMailingAddress().setApplyId(applyEntity.getId());
					entity.getMailingAddress().setUid(entity.getUid());
					GenericityUtil.setDate(entity.getMailingAddress());
					cardApplySendDao.insert(entity.getMailingAddress());
				}
				if(entity.getMailingAddressStr() != null && entity.getMailingAddressStr().length() > 0) {
					JSONObject object = JSONObject.parseObject(entity.getMailingAddressStr());
					CardApplySendEntity applySendEntity = JSONObject.parseObject(object.get("mailingAddress").toString(), CardApplySendEntity.class);
					applySendEntity.setApplyId(applyEntity.getId());
					applySendEntity.setUid(entity.getUid());
					GenericityUtil.setDate(applySendEntity);
					cardApplySendDao.insert(applySendEntity);
				}
				//kyc认证信息保存
				if(GenericityUtil.hasContent(entity.getKycData())) {
					entity.getKycData().setApplyId(applyEntity.getId());
					entity.getKycData().setUid(entity.getUid());
					GenericityUtil.setDate(entity.getKycData());
					cardApplyKycDao.insert(entity.getKycData());
				}else if(entity.getKycDataStr() != null && entity.getKycDataStr().length() > 0) {
					JSONObject object = JSONObject.parseObject(entity.getKycDataStr());
					CardApplyKycEntity applyKycEntity = JSONObject.parseObject(object.get("kycData").toString(), CardApplyKycEntity.class);
					applyKycEntity.setApplyId(applyEntity.getId());
					applyKycEntity.setUid(entity.getUid());
					GenericityUtil.setDate(applyKycEntity);
					cardApplyKycDao.insert(applyKycEntity);
				}else {
					CardApplyKycEntity kycEntity = cardApplyKycDao.findUid(entity.getUid());
					if(kycEntity != null) {
						if(kycEntity.getApplyId() == null) {
							kycEntity.setApplyId(applyEntity.getId());
							cardApplyKycDao.updateById(kycEntity);	
						}else {
							kycEntity.setApplyId(applyEntity.getId());
							kycEntity.setUid(entity.getUid());
							GenericityUtil.setDate(kycEntity);
							cardApplyKycDao.insert(kycEntity);							
						}
					}
				}
				//申请记录持卡人信息保存
				CardApplyManEntity cardApplyManEntity = new CardApplyManEntity();
				BeanUtils.copyProperties(cardholderEntity, cardApplyManEntity);
				cardApplyManEntity.setId(null);
				cardApplyManEntity.setApplyId(applyEntity.getId());
				GenericityUtil.setDate(cardApplyManEntity);
				cardApplyManDao.insert(cardApplyManEntity);
				return applyEntity;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		@Override
		@Synchronized
		public ResponseBase topUpV2(RechargeCardEntity entity) {
			try {
				entity.setAmount(entity.getTargetAmount());
				AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
				AppUserCardEntity userCard = appUserCardDao.findUserBankcardIdOrUid(entity.getUserBankcardId(),
						entity.getUid());
				if (userEntity != null && userCard != null) {
					CardEntity cardEntity = cardDao.findByuuId(userCard.getCardUuid());
					//实体卡未激活无法充值
					if(CardTypeEnums.PHYSICAL.getName().equals(userCard.getCardType())) {
						if (!CardStateEnums.ACTIVE.getIndex().toString().equals(userCard.getStatus())) {
							return setResultError(I18nUtil.getMessage("card_no_active"));
						}
					}
					// 校验
					ResponseBase chekBase = this.chekTouUp(cardEntity, entity, userEntity);
					if (!Constants.HTTP_RES_CODE_200.equals(chekBase.getRtncode())) {
						return chekBase;
					}
					// 判断是否是首次充值，首次充值需要添加开卡费校验
					ResponseBase chekOpendCardBase = this.chekOpendCardAmount(entity, userEntity, cardEntity);
					if (!Constants.HTTP_RES_CODE_200.equals(chekOpendCardBase.getRtncode())) {
						return chekOpendCardBase;
					}
					// 调用三方充值
					String orderNum = OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid()));
					entity.setRequestOrderId(orderNum);
					ResponseBase base = UCardApi.postData(entity.getUid(), UCardMethods.RECHARGE_BANKCARD, entity,
							orderNum);
					if (Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
						// 生成预订单卡充值记录
						CardTransactionEntity transaction = this.addTransaction(userCard, entity, orderNum);
						cardTransactionDao.insert(transaction);
						if (PayTypeEnums.WALLET.getIndex().equals(entity.getPayType())) {
							if(entity.getIsFirst()) {//首次充值添加开卡钱包记录
								// 生成预订单钱包消费记录(开卡)
								this.addOpenCardWalletLog(userCard, entity, userEntity, orderNum,
										WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getCnName(),
										WalletTitleTypeEnums.OPEN_CARD_VIRTUAL.getIndex());
							}
							// 生成预订单钱包消费记录(充值)
							WalletLogEntity walletLogEntity = this.addWalletLog(userCard, entity, userEntity, orderNum,
									WalletTitleTypeEnums.CARD_TOP_UP.getCnName(),
									WalletTitleTypeEnums.CARD_TOP_UP.getIndex());
							walletLogDao.insert(walletLogEntity);
							// 预减钱包余额
							Double monery = userEntity.getWalletBalance() == null ? 0 : userEntity.getWalletBalance();
							Double target = entity.getTargetAmount() == null ? 0 : entity.getTargetAmount();
							Double fee = entity.getHandlingFees() == null ? 0 : entity.getHandlingFees();
							Double openCardAmount = entity.getOpenCardAmount() == null ? 0 : entity.getOpenCardAmount();// 开卡费
							Double temp = monery - (target + fee + openCardAmount);
							userEntity.setWalletBalance(temp);
							appUserDao.updateById(userEntity);
						}
						return setResultSuccess(I18nUtil.getMessage("topUp_result"));
					}
					return base;
				} else {
					return setResultError(I18nUtil.getMessage("base_error"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		/**
		 * @category 充值前置校验
		 * @param cardEntity
		 * @param entity
		 * @param userEntity
		 * @return
		 */
		public ResponseBase chekTouUp(CardEntity cardEntity, RechargeCardEntity entity, AppUserEntity userEntity) {
			if (cardEntity == null) {
				return setResultError(I18nUtil.getMessage("bank_card_null"));
			}
			if (entity.getType() == null || entity.getType()) {
				if (userEntity.getPayPassword() != null && userEntity.getPayPassword().length() > 0) {
					if (!DigestUtils.md5DigestAsHex((entity.getPayPasswrod()).getBytes())
							.equals(userEntity.getPayPassword())) {
						return setResultError(I18nUtil.getMessage("pay_password_error"));
					}
				} else {
					return setResult(Constants.HTTP_RES_CODE_602, I18nUtil.getMessage("pay_password_error"), null);

				}
			}
			
			// 充值金额限制
			Double minLimi = cardEntity.getRechargeMinLimit() == null ? 0 : cardEntity.getRechargeMinLimit();
			if (minLimi >= ((entity.getTargetAmount() == null ? 0 : entity.getTargetAmount())
					+ entity.getHandlingFees())) {
				return setResultError(I18nUtil.getMessage("topup_money_min"));
			}
			Double maxLimit = cardEntity.getRechargeMaxLimit() == null ? 0 : cardEntity.getRechargeMaxLimit();
			if (maxLimit <= ((entity.getTargetAmount() == null ? 0 : entity.getTargetAmount())
					+ entity.getHandlingFees())) {
				return setResultError(I18nUtil.getMessage("topup_money_max"));
			}
			return setResultSuccess();
		}

		/**
		 * @category 首次充值开卡费处理
		 * @param entity
		 * @param userEntity
		 * @param cardEntity
		 * @return
		 */
		public ResponseBase chekOpendCardAmount(RechargeCardEntity entity, AppUserEntity userEntity,
				CardEntity cardEntity) {
			AppUserCardOpenEntity appUserCardOpenEntity = appUserCardOpenDao.findState(entity.getUid(),
					entity.getUserBankcardId(), PublicEnums.TOW.getIndex());
			if (appUserCardOpenEntity != null) {// 已经首次充值过不收开卡费
				entity.setOpenCardAmount(Constants.ZERO);
				entity.setIsFirst(false);
			} else {
				// 判断获取银行卡开卡费用
				entity.setIsFirst(true);
				ResponseBase cardBase = this.findOpenCardFee(entity.getUid(), cardEntity.getUuid());
				if (HTTP_RES_CODE_200.equals(cardBase.getRtncode())) {
					entity.setOpenCardAmount(Double.parseDouble(cardBase.getData().toString()));
				}
			}
			if (PayTypeEnums.WALLET.getIndex().equals(entity.getPayType())) {// 钱包充值
				// 检查余额
				if (userEntity == null || userEntity.getWalletBalance() <= 0) {
					return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
				}
				if (userEntity != null && userEntity.getWalletBalance() > 0) {
					Double monery = userEntity.getWalletBalance();// 钱包余额
					Double targetAmount = entity.getTargetAmount() == null ? 0 : entity.getTargetAmount();// 充值金额
					Double handlingFees = entity.getHandlingFees() == null ? 0 : entity.getHandlingFees();// 手续费
					Double openCardAmount = entity.getOpenCardAmount() == null ? 0 : entity.getOpenCardAmount();// 开卡费
					Double consume = targetAmount + handlingFees + openCardAmount;
					Double temp = monery - consume;
					if (temp < 0) {
						return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
					}
					//校验商户余额是否充足
					ResponseBase meBase = UCardApi.postData(null, UCardMethods.MERCHANT_ASSET, new Object(), null);
					if(!HTTP_RES_CODE_200.equals(meBase.getRtncode())) {
						return meBase;
					}
					if (meBase.getData() != null) {
						MerchantAssetsEntity assetsEntity = JSON.parseObject(meBase.getData().toString(), MerchantAssetsEntity.class);
						if(assetsEntity != null && assetsEntity.getAvailableAmount() != null) {
							if(assetsEntity.getAvailableAmount() <= consume) {
								//飞机消息
								messageTemplateServiceImpl.robotSendAsync(
										MessageEnums.ROBOT_MERCHANT_ZH.getIndex(),
										LanguageEnums.ZH_CN.getName(),
										RobotRoleEnums.FINANCE.getIndex(),
										assetsEntity.getAvailableAmount(),
										consume);
								return setResult(Constants.HTTP_RES_CODE_500, I18nUtil.getMessage("Merchants_money_scarcity"), null);
							}
						}
					}
					
				}
			}
			return setResultSuccess(entity);
		}

		/**
		 * @category 新增开卡记录
		 * @param userCard
		 * @param entity
		 * @param userEntity
		 * @param orderNum
		 * @param typeName
		 * @param typeId
		 * @return
		 */
		public WalletLogEntity addOpenCardWalletLog(AppUserCardEntity userCard,RechargeCardEntity entity,AppUserEntity userEntity,String orderNum,String typeName,Integer typeId) {
			try {
				WalletLogEntity walletLogEntity = new WalletLogEntity();
				walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid())));
				walletLogEntity.setOutOrderNo(orderNum);
				walletLogEntity.setUserId(userEntity.getId());
				walletLogEntity.setUid(userEntity.getUid());
				walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
				walletLogEntity.setTitle(typeName);
				walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
				walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
				walletLogEntity.setRealMoney(BigDecimal.valueOf(entity.getOpenCardAmount()));
				walletLogEntity.setServiceCharge(BigDecimal.valueOf(0.0));
				walletLogEntity.setFormName(userEntity.getUserEmail());
				walletLogEntity.setFormAccount(userEntity.getUid());
				walletLogEntity.setToName(userCard.getCardNo());
				walletLogEntity.setToAccount(userCard.getCardNo());
				walletLogEntity.setUserBankcardId(userCard.getUserBankcardId());
				walletLogEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
				walletLogEntity.setOperateType(typeId);
				walletLogEntity.setSetUser(entity.getSetUser());
				walletLogEntity.setSetUserName(entity.getSetUserName());
				GenericityUtil.setDate(walletLogEntity);
				walletLogDao.insert(walletLogEntity);
				//新增开卡记录
				AppUserCardOpenEntity appUserCardOpenEntity = new AppUserCardOpenEntity();
				appUserCardOpenEntity.setOpenCardAmount(entity.getOpenCardAmount());
				appUserCardOpenEntity.setOpenState(PublicEnums.ONE.getIndex());
				appUserCardOpenEntity.setOrderNum(orderNum);
				appUserCardOpenEntity.setUid(entity.getUid());
				appUserCardOpenEntity.setUserBankcardId(entity.getUserBankcardId());
				GenericityUtil.setDate(appUserCardOpenEntity);
				appUserCardOpenDao.insert(appUserCardOpenEntity);
				//修改银行卡、申请记录状态
				AppUserCardEntity appUserCardEntity = appUserCardDao.findUserBankcardId(entity.getUserBankcardId());
				if(appUserCardEntity != null) {
					if(CardStateEnums.TBA.getIndex().toString().equals(appUserCardEntity.getStatus())) {
						appUserCardEntity.setStatus(CardStateEnums.ACTIVE_PROCESSING.getIndex().toString());
						appUserCardDao.updateById(appUserCardEntity);						
					}
					CardApplyEntity applyEntity = cardApplyDao.selectById(appUserCardEntity.getApplyId());
					if(applyEntity != null && !OpenCardStateEnums.SUCCESS_ACTIVATION.getIndex().equals(applyEntity.getApplyState())) {
						applyEntity.setApplyState(OpenCardStateEnums.PROCESS_ACTIVATION.getIndex());
						applyEntity.setApplyStateName(OpenCardStateEnums.PROCESS_ACTIVATION.getName());
						cardApplyDao.updateById(applyEntity);
					}
				}
				return walletLogEntity;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		@Override
		public ResponseBase checkFirstTopUp(String uid, Integer userBankcardId,String cardUUid) {
			try {
				AppUserCardEntity appUserCardEntity = appUserCardDao.findUserBankcardId(userBankcardId);
				if(appUserCardEntity != null && CardStateEnums.ACTIVE.getIndex().toString().equals(appUserCardEntity.getStatus()) ) {
					AppUserCardOpenEntity appUserCardOpenEntity = appUserCardOpenDao.findState(uid,userBankcardId, PublicEnums.TOW.getIndex());
					if(appUserCardOpenEntity == null) {
						AppUserCardOpenEntity cardOpenEntity = new AppUserCardOpenEntity();
						cardOpenEntity.setOpenCardAmount(Constants.ZERO);
						cardOpenEntity.setOpenState(PublicEnums.TOW.getIndex());
						cardOpenEntity.setOrderNum(OrderCodeFactory.getOrderCode(Long.parseLong(appUserCardEntity.getUid())));
						cardOpenEntity.setUid(appUserCardEntity.getUid());
						cardOpenEntity.setUserBankcardId(appUserCardEntity.getUserBankcardId());
						GenericityUtil.setDate(cardOpenEntity);
						appUserCardOpenDao.insert(cardOpenEntity);						
					}
				}
				CheckFirstTopUpEntity checkFirstTopUpEntity = new CheckFirstTopUpEntity();
				AppUserCardOpenEntity appUserCardOpenEntity = appUserCardOpenDao.findState(uid,userBankcardId, PublicEnums.TOW.getIndex());
				if (appUserCardOpenEntity != null) {// 已经首次充值过
					checkFirstTopUpEntity.setIsFirst(false);
				} else {
					checkFirstTopUpEntity.setIsFirst(true);
				}
				// 判断获取银行卡开卡费用
				ResponseBase cardBase = this.findOpenCardFee(uid, cardUUid);
				if (HTTP_RES_CODE_200.equals(cardBase.getRtncode())) {
					checkFirstTopUpEntity.setOpenCardAmount(Double.parseDouble(cardBase.getData().toString()));
				}
				return setResultSuccess(checkFirstTopUpEntity, I18nUtil.getMessage("base_success"));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		
		@Override
		public ResponseBase upState(String uuid, Integer stateId) {
			try {
				CardEntity cardEntity = cardDao.findByuuId(uuid);
				if(cardEntity != null) {
					cardEntity.setCardState(stateId);
					cardDao.updateState(uuid,stateId);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}
				return setResultError(I18nUtil.getMessage("base_error"));
			} catch (Exception e) {
				e.printStackTrace();
				return setResultError(Constants.ERROR);
			}
		}
		
}
