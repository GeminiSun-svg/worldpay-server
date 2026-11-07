package com.worldpay.service.impl;

import com.worldpay.utils.api.MonitorUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.GetWalletAddressEntity;
import com.worldpay.api.request.WithdrawalCallbackEntity;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.api.response.StatBackEntity;
import com.worldpay.api.response.UsdtTopinNotifyEntity;
import com.worldpay.api.response.UserAddressReq;
import com.worldpay.api.response.Web3AddressResEntity;
import com.worldpay.constants.UsdtTopinMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserWebAddressDao;
import com.worldpay.dao.system.SysInfoDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.dao.wallet.WalletManualLogDao;
import com.worldpay.dao.wallet.WalletNetworkTopUpDao;
import com.worldpay.dao.wallet.WalletToWebLogDao;
import com.worldpay.dao.wallet.WalletTransfetListDao;
import com.worldpay.dao.wallet.WalletTransfetRatesDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserWebAddressEntity;
import com.worldpay.entity.response.TransferReadingEntity;
import com.worldpay.entity.system.SysInfoEntity;
import com.worldpay.query.PublicQueryEntity;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.IpUtil;
import com.worldpay.utils.OrderCodeFactory;
import lombok.Synchronized;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.service.WalletService;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.entity.wallet.WalletManualLogEntity;
import com.worldpay.entity.wallet.WalletNetworkTopUpEntity;
import com.worldpay.entity.wallet.WalletToWebLogEntity;
import com.worldpay.entity.wallet.WalletTransfetListEntity;
import com.worldpay.entity.wallet.WalletTransfetRatesEntity;
import com.worldpay.enums.ApplicationRecordStateEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.NetworkTypeEnums;
import com.worldpay.enums.NoticeStateEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.PayTypeEnums;
import com.worldpay.enums.PublicEnums;
import com.worldpay.enums.SysConfigTypeEnums;
import com.worldpay.enums.TokenTypeEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.UserStateEnums;
import com.worldpay.enums.WalletTitleTypeEnums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
@CrossOrigin
public class WalletServicelmpl extends BaseApiService implements WalletService {
	
    @Autowired
    private WalletLogDao walletLogDao;
    
    @Autowired
    private AppUserDao appUserDao;
    
    @Autowired
    private MessageTemplateServiceImpl messageTemplateServiceImpl;
    
    @Autowired
    private WalletManualLogDao walletManualLogDao;
    
    @Autowired
	private WalletTransfetRatesDao walletTransfetRatesDao;
    
    @Autowired
    private WalletTransfetListDao walletTransfetListDao;
    
    @Autowired
    private WalletNetworkTopUpDao walletNetworkTopUpDao;
    
    @Autowired
    private AppUserWebAddressDao appUserWebAddressDao;
    
    @Autowired
    private SysInfoDao sysInfoDao;
    
    @Autowired
    private WalletToWebLogDao walletToWebLogDao;
    
    @Autowired
	private IpUtil ipUtil;

    @Override
    @SysLogAnnotation(module = "钱包接口", type = "get", remark = "获取USDT充值的钱包地址")
    public ResponseBase topinUsdtAddress(String uid) {
        try {
            AppUserEntity entity = appUserDao.findByUid(uid);
            if(entity != null ) {
            	//查询用户是否已存在地址，已存在直接返回
            	AppUserWebAddressEntity accountWebAddressEntity = appUserWebAddressDao.findByUid(uid);
    			if(accountWebAddressEntity != null) {
    				List<UserAddressReq> list = this.formatAddress(accountWebAddressEntity);
    				return setResultSuccess(list);
    			}
                
                GetWalletAddressEntity getWalletAddressapi = new GetWalletAddressEntity();
                getWalletAddressapi.setUid(uid);
                getWalletAddressapi.setEmail(entity.getUserEmail());
                //请求接口 返回地址 存储到数据库
                ResponseBase resData = MonitorUtil.walletPost(UsdtTopinMethods.getAddressurl, getWalletAddressapi);
                if(resData.getRtncode() == 200) {
                	String resStr = JSON.toJSONString(resData.getData());
	            	Web3AddressResEntity resEntity = JSONObject.parseObject(resStr, Web3AddressResEntity.class);
	            	AppUserWebAddressEntity addressEntity = new AppUserWebAddressEntity();
	            	addressEntity.setBnbAddress(resEntity.getBnbAddress());
	            	addressEntity.setBtcAddress(resEntity.getBtcAddress());
	            	addressEntity.setEthAddress(resEntity.getEthAddress());
	            	addressEntity.setTronAddress(resEntity.getTronAddress());
	            	addressEntity.setUid(entity.getUid());
	            	addressEntity.setUserEmail(entity.getUserEmail());
	            	GenericityUtil.setDate(addressEntity);
	            	appUserWebAddressDao.insert(addressEntity);
	            	List<UserAddressReq> list = this.formatAddress(addressEntity);
	            	return setResultSuccess(list);
                }
            }
                return setResultError(I18nUtil.getMessage("base_error"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    public List<UserAddressReq> formatAddress(AppUserWebAddressEntity accountWebAddressEntity) {
    	List<UserAddressReq> list = new ArrayList<UserAddressReq>();
		String language = LanguageContext.getLanguage();
		SysInfoEntity entity = sysInfoDao.findContent(SysConfigTypeEnums.WALLET_CENTER.getIndex(),NoticeStateEnums.NORMAL.getIndex(),language);
		if(accountWebAddressEntity.getTronAddress() != null && !accountWebAddressEntity.getTronAddress().isEmpty()) {
			UserAddressReq tron = new UserAddressReq();
			tron.setAddress(accountWebAddressEntity.getTronAddress());
			tron.setAddressType(NetworkTypeEnums.TRON.getName());
			if(entity != null) {
				tron.setCountInfo(entity.getConfigContent());
				tron.setCountUrl(entity.getConfigUrl());
			}
			list.add(tron);
		}
		if(accountWebAddressEntity.getBnbAddress() != null && !accountWebAddressEntity.getBnbAddress().isEmpty()) {
			UserAddressReq bnb = new UserAddressReq();
			bnb.setAddress(accountWebAddressEntity.getBnbAddress());
			bnb.setAddressType(NetworkTypeEnums.BNB.getLable());
			if(entity != null) {
				bnb.setCountInfo(entity.getConfigContent());
				bnb.setCountUrl(entity.getConfigUrl());
			}
			list.add(bnb);
		}
//		if(accountWebAddressEntity.getEthAddress() != null && !accountWebAddressEntity.getEthAddress().isEmpty()) {
//			UserAddressReq eth = new UserAddressReq();
//			eth.setAddress(accountWebAddressEntity.getEthAddress());
//			eth.setAddressType(NetworkTypeEnums.ETH.getName());
//			if(entity != null) {
//				eth.setCountInfo(entity.getConfigContent());
//				eth.setCountUrl(entity.getConfigUrl());
//			}
//			list.add(eth);
//		}
		
//		if(accountWebAddressEntity.getBtcAddress() != null && !accountWebAddressEntity.getBtcAddress().isEmpty()) {
//			UserAddressReq btc = new UserAddressReq();
//			btc.setAddress(accountWebAddressEntity.getBtcAddress());
//			btc.setAddressType(NetworkTypeEnums.BTC.getName());
//			list.add(btc);
//		}
		return list;
    }
    
    
	@Override
	public ApiResponseEntity topinUsdtNotify(@RequestBody UsdtTopinNotifyEntity entity,HttpServletRequest req) {
		try {
			 // 获取客户端 IP
//	        String ipAddress = ipUtil.getClientIp(req);
//	        if(!Constants.MSG_IP_LIST.contains(ipAddress)) {
//				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "ip地址不在白名单", null);
//	        }
        	// 验签
			String sign = entity.getSign();
			entity.setSign(null);
			boolean temp = MonitorUtil.Verification(entity, sign);
			if(temp == false) {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "验签不通过", null);
			}
			if(!"in".equals(entity.getType())) {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_200, null, null);
			}
			if(!TokenTypeEnums.USDC.getName().equals(entity.getCoin()) 
					&& !TokenTypeEnums.USDT.getName().equals(entity.getCoin())) {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_200, null, null);
			}
			WalletLogEntity logEntity = walletLogDao.findHash(entity.getHash());
			if(logEntity != null) {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "订单已存在", null);
			}
			AppUserEntity appUserEntity = appUserDao.findByUid(entity.getUid());
			if(appUserEntity == null) {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "用户不存在", null);
			}
			
			// 计算新的余额
			Double currentBalance = appUserEntity.getWalletBalance() != null
					? appUserEntity.getWalletBalance()
					: Constants.ZERO;
			Double newBalance = currentBalance + Double.parseDouble(entity.getAmount());
			appUserEntity.setWalletBalance(newBalance);
			//更新余额
			appUserDao.updateById(appUserEntity);
			//新增钱包记录
			this.addCallbackLog(entity,appUserEntity);
			
			//发送通知
			messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.WALLET_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					entity.getUid(), 
					entity.getAmount()+TransCurrencyEnums.USD.getName(), 
					newBalance+TransCurrencyEnums.USD.getName(), 
					appUserEntity.getFreezeBalance()+TransCurrencyEnums.USD.getName());
    
			return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_200, Constants.SUCCESS, null);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	//充值回调记录新增
		public void addCallbackLog(UsdtTopinNotifyEntity entity, AppUserEntity appUserEntity) {
			try {
				WalletLogEntity walletLogEntity = new WalletLogEntity();
				walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid())));
				walletLogEntity.setOutOrderNo(entity.getHash());
				walletLogEntity.setUid(entity.getUid());
				walletLogEntity.setUserId(appUserEntity.getId());
				walletLogEntity.setTradeType(1);
				walletLogEntity.setTitle(WalletTitleTypeEnums.WALLET_TOP_UP.getCnName());
				walletLogEntity.setPrimevalMoney(appUserEntity.getWalletBalance());
				walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
				walletLogEntity.setRealMoney(BigDecimal.valueOf(Double.parseDouble(entity.getAmount())));
				walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));
				walletLogEntity.setFormAccount(entity.getOutaddress());
				walletLogEntity.setToName(entity.getEmail());
				walletLogEntity.setToAccount(entity.getInaddress());
				walletLogEntity.setStatus(PublicEnums.TOW.getIndex() + "");
				walletLogEntity.setSetTime(new Date());
				walletLogEntity.setGmtModified(new Date());
				walletLogEntity.setOperateType(PublicEnums.ONE.getIndex());
				walletLogDao.insert(walletLogEntity);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

    @Override
    @SysLogAnnotation(module = "钱包接口", type = "post", remark = "获取钱包交易记录")
    public ResponseBase walletLog(@RequestBody WalletLogEntity entity){
        try{
        	String languag = LanguageContext.getLanguage();
            PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
            List<WalletLogEntity> list= walletLogDao.findWalletLogsByConditions(entity);
            if(list != null && list.size() > 0) {
            	for (int i = 0; i < list.size(); i++) {
					AppUserEntity userEntity = appUserDao.findByUid(list.get(i).getUid());
					if(userEntity != null) {
						list.get(i).setUserEmail(userEntity.getUserEmail());
						list.get(i).setOldMonery(list.get(i).getRealMoney());
//						list.get(i).setRealMoney(list.get(i).getRealMoney().add(list.get(i).getServiceCharge()));
						BigDecimal realMoney = list.get(i).getRealMoney() == null
						        ? BigDecimal.ZERO
						        : list.get(i).getRealMoney();

						BigDecimal serviceCharge = list.get(i).getServiceCharge() == null
						        ? BigDecimal.ZERO
						        : list.get(i).getServiceCharge();

						list.get(i).setRealMoney(realMoney.add(serviceCharge));
					}
					list.get(i).setTitle(WalletTitleTypeEnums.getLanguagName(list.get(i).getOperateType(), languag));
				}
            }
            PageInfo<WalletLogEntity> info = new PageInfo<>(list);
            return setResultSuccess(info, I18nUtil.getMessage("base_success"));
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @Override
    @SysLogAnnotation(module = "钱包接口", type = "post", remark = "获取钱包交易记录统计")
    public ResponseBase walletLogStat(@RequestBody PublicQueryEntity entity){
        try{
            //获取统计的值和对应的订单列表
            PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
            // 查询统计数据
            StatBackEntity statData = walletLogDao.findWalletLogStatByConditions(entity);

            // 查询订单列表
            List<WalletLogEntity> orderList = walletLogDao.tfindWalletLogsByConditions(entity);

            // 设置订单列表到统计数据中
            statData.setOrderList(orderList);

            // 获取分页信息
            PageInfo<StatBackEntity> info = new PageInfo<>(Collections.singletonList(statData));
            return setResultSuccess(info, I18nUtil.getMessage("base_success"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
   
	@Override
	public ResponseBase walletTopUp(@RequestBody WalletManualLogEntity entity,HttpServletRequest req) {
		try {
			AppUserEntity appUser = appUserDao.findByUid(entity.getUid());
			if(appUser == null) {
				return setResultError(I18nUtil.getMessage("user.account_error"));
			}
			if(UserStateEnums.LOGOUT.getIndex().equals(appUser.getUserState())) {
				return setResultError(I18nUtil.getMessage("user.account_null"));
			}
			boolean temp = false;
			if(entity.getNetworkTopUpId() != null) {
				WalletNetworkTopUpEntity topUpEntity = walletNetworkTopUpDao.selectById(entity.getNetworkTopUpId());
				if(topUpEntity != null) {
					entity.setWalletAddress(topUpEntity.getWalletAddress());
					entity.setWalletType(topUpEntity.getNetworkType());
					temp = true;
				}
			}else {
				entity.setWalletType(NetworkTypeEnums.TRON.getName());
				entity.setWalletAddress(appUser.getTronUsdtAcount());
			}
			//手动充值记录
			String orderNo = OrderCodeFactory.getOrderCode(Long.parseLong(appUser.getUid()));
			entity.setOrderNo(orderNo);
			entity.setUserId(appUser.getId());
			entity.setUserEmail(appUser.getUserEmail());
			entity.setUserTel(appUser.getMobileNumber());
			entity.setTopupAmountForward(appUser.getWalletBalance());
			entity.setTopupAmountBack(appUser.getWalletBalance() + entity.getTopupAmount());
			entity.setOperateUserIp(ipUtil.getClientIp(req));
			GenericityUtil.setDate(entity);
			walletManualLogDao.insert(entity);
			// 添加钱包交易记录
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(orderNo);
			walletLogEntity.setOutOrderNo(orderNo);
			walletLogEntity.setUid(entity.getUid());
			walletLogEntity.setUserId(appUser.getId());
			walletLogEntity.setPrimevalMoney(appUser.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			if(entity.getTopupAmount() < 0) {
				walletLogEntity.setTitle(WalletTitleTypeEnums.WALLET_TDEDUCTIONS.getCnName());
				walletLogEntity.setOperateType(WalletTitleTypeEnums.WALLET_TDEDUCTIONS.getIndex());
				walletLogEntity.setTradeType(2);
				walletLogEntity.setRealMoney(BigDecimal.valueOf(Math.abs(entity.getTopupAmount())));
			}else {
				walletLogEntity.setTitle(WalletTitleTypeEnums.WALLET_TOP_UP.getCnName());
				walletLogEntity.setOperateType(WalletTitleTypeEnums.WALLET_TOP_UP.getIndex());
				walletLogEntity.setTradeType(1);
				walletLogEntity.setRealMoney(BigDecimal.valueOf(entity.getTopupAmount()));
			}
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));
			walletLogEntity.setFormAccount(PayTypeEnums.MANUAL_TRANSFER.getName());
			if(temp) {
				walletLogEntity.setToAccount(appUser.getUserEmail());
			}else {
				walletLogEntity.setToAccount(appUser.getTronUsdtAcount());
			}
			walletLogEntity.setStatus(PublicEnums.TOW.getIndex() + "");
			walletLogEntity.setSetTime(new Date());
			walletLogEntity.setGmtModified(new Date());
			walletLogEntity.setSetUser(entity.getOperateUserId());
			walletLogEntity.setSetUserName(entity.getOperateUserName());
			walletLogDao.insert(walletLogEntity);
			//修改用户余额
			appUser.setWalletBalance(entity.getTopupAmountBack());
			appUserDao.updateById(appUser);
			//修改记录状态
			if(temp) {
				WalletNetworkTopUpEntity topUpEntity = walletNetworkTopUpDao.selectById(entity.getNetworkTopUpId());
				if(topUpEntity != null) {
					topUpEntity.setOrderState(OrderStateEnums.POSTED.getIndex());
					topUpEntity.setOrderStateName(OrderStateEnums.POSTED.getZhName());
					walletNetworkTopUpDao.updateById(topUpEntity);
				}
			}
			//发送通知
			messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.WALLET_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					entity.getUid(), 
					entity.getTopupAmount()+TransCurrencyEnums.USD.getName(), 
					appUser.getWalletBalance()+TransCurrencyEnums.USD.getName(), 
					appUser.getFreezeBalance()+TransCurrencyEnums.USD.getName());
			
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findWalletLog(@RequestBody WalletLogEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<WalletLogEntity> list = walletLogDao.findWalletLogsByConditions(entity);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					AppUserEntity userEntity = appUserDao.findByUid(list.get(i).getUid());
					if (userEntity != null) {
						list.get(i).setUserEmail(userEntity.getUserEmail());
						list.get(i).setOldMonery(list.get(i).getRealMoney());
//						list.get(i).setRealMoney(list.get(i).getRealMoney().add(list.get(i).getServiceCharge()));
						BigDecimal realMoney = list.get(i).getRealMoney() == null
						        ? BigDecimal.ZERO
						        : list.get(i).getRealMoney();

						BigDecimal serviceCharge = list.get(i).getServiceCharge() == null
						        ? BigDecimal.ZERO
						        : list.get(i).getServiceCharge();

						list.get(i).setRealMoney(realMoney.add(serviceCharge));
					}
				}
			}
			PageInfo<WalletLogEntity> info = new PageInfo<>(list);
			// 获取所有符合条件的realmoney字段的和
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@Synchronized
	public ResponseBase transfer(@RequestBody WalletTransfetListEntity entity) {
		try {
			//查询发送方是否存在
			AppUserEntity userEntity = appUserDao.findByUid(entity.getSendUid());
			if(userEntity == null) {
				return setResultError(I18nUtil.getMessage("account_error")); 
			}
			//接收方信息
			AppUserEntity recipientUser = appUserDao.findByEmail(entity.getRecipientEmail());
			if(recipientUser == null) {
				return setResultError(I18nUtil.getMessage("recipient_account_null")); 
			}
			//校验支付密码
			if(entity.getPayPassword() != null && entity.getPayPassword().length() > 0) {
				if (!DigestUtils.md5DigestAsHex((entity.getPayPassword()).getBytes())
						.equals(userEntity.getPayPassword())) {
					return setResultError(I18nUtil.getMessage("pay_password_error"));
				}
			}else {
//				return setResultError(I18nUtil.getMessage("pay_password_null"));
				return setResult(Constants.HTTP_RES_CODE_602, I18nUtil.getMessage("pay_password_null"), null);
			}
			//判断发送方余额是否充足
			if(userEntity == null || userEntity.getWalletBalance() <= 0) {
				return setResultError(I18nUtil.getMessage("wallet_Balance_null"));
			}
			if(userEntity != null && userEntity.getWalletBalance() > 0) {
				//账户可用余额
				Double monery = userEntity.getWalletBalance();
				Double temp = monery - ((entity.getSendMoney() == null ? 0 : entity.getSendMoney()));
				if(temp < 0) {
//					return setResultError(I18nUtil.getMessage("wallet_Balance_null"));
					return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
				}
			}
			
			//查询转账费率
			WalletTransfetRatesEntity ratesEntity = walletTransfetRatesDao.selectOne(null);
			if(ratesEntity == null || ratesEntity.getRates() == null) {
				return setResultError(I18nUtil.getMessage("transfet_rates_null"));
			}
			//计算手续费(添加记录)
			entity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(entity.getSendUid())));
			entity.setHandlingFee(entity.getSendMoney() * ratesEntity.getRates());
			entity.setSendRates(ratesEntity.getRates());
			entity.setActualMoney(entity.getSendMoney() - entity.getHandlingFee());
			entity.setSendForwardBalance(userEntity.getWalletBalance());
			entity.setSendBackBalance(userEntity.getWalletBalance() - entity.getSendMoney());
			entity.setRecipientForwardBalance(recipientUser.getWalletBalance());
			entity.setRecipientBackBalance(recipientUser.getWalletBalance() + entity.getActualMoney());
			entity.setSendEmail(userEntity.getUserEmail());
			entity.setRecipientUid(recipientUser.getUid());
			GenericityUtil.setDate(entity);
			walletTransfetListDao.insert(entity);
			//发送方钱包交易记录
			this.addWalletLog(userEntity, TradeTypeEnums.EXPENDITURE.getIndex(), entity,entity.getHandlingFee());
			//接收方钱包交易记录
			this.addWalletLog(recipientUser, TradeTypeEnums.INCOME.getIndex(), entity,0.0);
			//修改发送方余额
			userEntity.setWalletBalance(userEntity.getWalletBalance() - entity.getSendMoney());
			appUserDao.updateById(userEntity);
			//修改接收方余额
			recipientUser.setWalletBalance(recipientUser.getWalletBalance() + entity.getActualMoney());
			appUserDao.updateById(recipientUser);
			//发送方钱包余额变动通知
			this.sendMsg(userEntity, entity, entity.getHandlingFee());
			//接收方钱包余额变通通知
			this.receiveMsg(recipientUser, entity, entity.getHandlingFee());
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase transferReading(Double sendMoney, String uid) {
		try {
			//查询发送方是否存在
			AppUserEntity userEntity = appUserDao.findByUid(uid);
			if(userEntity == null) {
				return setResultError(I18nUtil.getMessage("user.account_null")); 
			}
			//判断发送方余额是否充足
			if(userEntity == null || userEntity.getWalletBalance() <= 0) {
				return setResultError(I18nUtil.getMessage("wallet_Balance_null"));
			}
			if(userEntity != null && userEntity.getWalletBalance() > 0) {
				//账户可用余额
				Double monery = userEntity.getWalletBalance() - (userEntity.getFreezeBalance() == null ? 0 : userEntity.getFreezeBalance());
				Double temp = monery - ((sendMoney == null ? 0 : sendMoney));
				if(temp <= 0) {
					return setResultError(I18nUtil.getMessage("wallet_Balance_null"));
				}
			}
			//查询转账费率
			WalletTransfetRatesEntity ratesEntity = walletTransfetRatesDao.selectOne(null);
			if(ratesEntity == null || ratesEntity.getRates() == null) {
				return setResultError(I18nUtil.getMessage("transfet_rates_null"));
			}
			TransferReadingEntity transferReadingEntity = new TransferReadingEntity();
			transferReadingEntity.setSendRates(ratesEntity.getRates());
			transferReadingEntity.setHandlingFee(sendMoney * ratesEntity.getRates());
			transferReadingEntity.setActualMoney(sendMoney - transferReadingEntity.getHandlingFee());
			return setResultSuccess(transferReadingEntity,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findReading() {
		WalletTransfetRatesEntity ratesEntity = walletTransfetRatesDao.selectOne(null);
		return setResultSuccess(ratesEntity,I18nUtil.getMessage("base_success"));
	}
	
	
	public void addWalletLog(AppUserEntity userEntity,Integer tradeType,WalletTransfetListEntity entity,Double handlingFee) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
			walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(tradeType);
			walletLogEntity.setTitle(WalletTitleTypeEnums.INTERNAL_TRANSFER.getCnName());
			walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(entity.getActualMoney()));
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(handlingFee));
			walletLogEntity.setFormName(entity.getSendEmail());
			walletLogEntity.setFormAccount(entity.getSendUid());
			walletLogEntity.setToName(entity.getRecipientUid());
			walletLogEntity.setToAccount(entity.getRecipientEmail());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.INTERNAL_TRANSFER.getIndex());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	public void sendMsg(AppUserEntity userEntity,WalletTransfetListEntity entity,Double handlingFee) {
		
		//发送通知
		messageTemplateServiceImpl.unifiedSendAsync(
				MessageEnums.WALLET_ZH.getIndex(), 
				LanguageContext.getLanguage(),
				userEntity.getUid(), 
				"-"+entity.getSendMoney()+TransCurrencyEnums.USD.getName(),
				entity.getSendBackBalance()+TransCurrencyEnums.USD.getName(),
				userEntity.getFreezeBalance()+TransCurrencyEnums.USD.getName());
	}
	
	
	public void receiveMsg(AppUserEntity userEntity,WalletTransfetListEntity entity,Double handlingFee) {
		//发送通知
		messageTemplateServiceImpl.unifiedSendAsync(
				MessageEnums.WALLET_ZH.getIndex(), 
				LanguageContext.getLanguage(),
				userEntity.getUid(), 
				"+"+entity.getActualMoney()+TransCurrencyEnums.USD.getName(),
				entity.getRecipientBackBalance()+TransCurrencyEnums.USD.getName(),
				userEntity.getFreezeBalance()+TransCurrencyEnums.USD.getName());
	}
	
	
	@Override
	public ApiResponseEntity withdrawalNotify(@RequestBody WithdrawalCallbackEntity entity) {
		try {
			// 获取客户端 IP
//			String ipAddress = ipUtil.getClientIp(req);
//			if (!Constants.MSG_IP_LIST.contains(ipAddress)) {
//				return setResultError("IP地址没有访问权限");
//			return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "IP地址没有访问权限", null);
//			}
//			// 验签
			String sign = entity.getSign();
			entity.setSign(null);
			boolean temp = MonitorUtil.Verification(entity, sign);
			if(temp == false) {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "验签不通过", null);
			}
			WalletToWebLogEntity toWebLogEntity = walletToWebLogDao.findOrderNum(entity.getOutOrderNo());
			WalletLogEntity logEntity = walletLogDao.findOutOrderNo(entity.getOutOrderNo());
			if(toWebLogEntity != null && logEntity != null) {
				if(!toWebLogEntity.getWalletAddress().equals(entity.getAddress())) {
					return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "钱包地址不匹配", null);
				}
				if(Constants.API_RES_W.equals(entity.getStatus())) {//交易成功
					toWebLogEntity.setApplyState(ApplicationRecordStateEnums.AUDIT_PASS.getIndex());
					toWebLogEntity.setApplyStateName(ApplicationRecordStateEnums.AUDIT_PASS.getName());
					logEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
				}else{//交易失败
					toWebLogEntity.setApplyState(ApplicationRecordStateEnums.TRANSFER_FAILED.getIndex());
					toWebLogEntity.setApplyStateName(ApplicationRecordStateEnums.TRANSFER_FAILED.getName());
					logEntity.setStatus(OrderStateEnums.FAILED.getIndex().toString());
					//退回余额
					AppUserEntity appUserEntity = appUserDao.findByUid(toWebLogEntity.getUid());
					if(appUserEntity != null) {
						appUserEntity.setWalletBalance(appUserEntity.getWalletBalance() + toWebLogEntity.getTransferAmount());
						appUserDao.updateById(appUserEntity);
						//新增退回交易记录
						this.addReturnWalletLog(appUserEntity, TradeTypeEnums.INCOME.getIndex(), toWebLogEntity);
					}
				}
				//修改申请记录状态
				toWebLogEntity.setTranHash(entity.getTxHash());
				walletToWebLogDao.updateById(toWebLogEntity);
				//修改钱包交易记录状态
				logEntity.setTranHash(entity.getTxHash());
				walletLogDao.updateById(logEntity);
				//消息处理
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.WALLET_WITHDRAWAL_SUCCESS_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						toWebLogEntity.getUid(), 
						toWebLogEntity.getRealAmount() + TransCurrencyEnums.USD.getName(),
						toWebLogEntity.getWalletAddress());

				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_200, Constants.SUCCESS, null);
			}else {
				return ApiResponseEntity.setResult(Constants.HTTP_RES_CODE_500, "订单不存在", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	private void addReturnWalletLog(AppUserEntity appUserEntity, Integer tradeType, WalletToWebLogEntity toWebLogEntity) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(appUserEntity.getUid())));
			walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(appUserEntity.getUid())));
			walletLogEntity.setUserId(appUserEntity.getId());
			walletLogEntity.setUid(appUserEntity.getUid());
			walletLogEntity.setTradeType(tradeType);
			walletLogEntity.setTitle(WalletTitleTypeEnums.TRANSFER_OUT_WEB_ERROR.getCnName());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.TRANSFER_OUT_WEB_ERROR.getIndex());
			walletLogEntity.setPrimevalMoney(appUserEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(toWebLogEntity.getTransferAmount()));
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));
			walletLogEntity.setFormName(appUserEntity.getUserEmail());
			walletLogEntity.setFormAccount(appUserEntity.getUid());
			walletLogEntity.setToName(appUserEntity.getUserEmail());
			walletLogEntity.setToAccount(appUserEntity.getUid());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
}
