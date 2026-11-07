package com.worldpay.service.impl;

import com.worldpay.utils.api.MonitorUtil;
import com.worldpay.filter.JWTAuthenticationFilter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.api.request.WithdrawReqEntity;
import com.worldpay.api.response.WalletToWebResEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UsdtTopinMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.dao.wallet.WalletToWebLogDao;
import com.worldpay.dao.wallet.WalletToWebRatesDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.entity.wallet.WalletToWebLogEntity;
import com.worldpay.entity.wallet.WalletToWebRatesEntity;
import com.worldpay.enums.ApplicationRecordStateEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.RobotRoleEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.UserStateEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.enums.WalletToWebRatesEnums;
import com.worldpay.service.WalletToWebLogService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.GoogleAuthenticatorUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.OrderCodeFactory;

@Service
@Transactional
@CrossOrigin
public class WalletToWebLogServiceImpl extends BaseApiService implements WalletToWebLogService {

	@Autowired
	private WalletToWebLogDao walletToWebLogDao;

	@Autowired
	private AppUserDao appUserDao;

	@Autowired
	private WalletToWebRatesDao walletToWebRatesDao;

	@Autowired
	private WalletLogDao walletLogDao;
	
	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private AppUserServiceImpl appUserServiceImpl;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;


	@Override
	public ResponseBase create(@RequestBody WalletToWebLogEntity entity) {
	    try {
	        // 校验支付密码
	        ResponseBase passwordBase = appUserServiceImpl.checkPayPasswrod(entity.getUid(), entity.getPayPassword());
	        if (!Constants.HTTP_RES_CODE_200.equals(passwordBase.getRtncode())) {
	            return passwordBase;
	        }
	        // 信息校验
	        AppUserEntity accountEntity = appUserDao.findByUid(entity.getUid());
	        if (accountEntity == null) {
	            return setResultError(I18nUtil.getMessage("user.not_null"));
	        }
	        
	        if(accountEntity.getWalletBalance() < entity.getTransferAmount()) {
	        	return setResultError(I18nUtil.getMessage("wallet_Balance_null"));
	        }
	        // 使用 BigDecimal 处理所有金额计算
	        BigDecimal transferAmount = BigDecimal.valueOf(entity.getTransferAmount());
	        
	     // 获取费率配置
		    BigDecimal handlingRates = null;
		    BigDecimal serverAmount = null;
		    Double minAmount;
		    Double maxAmount;
		    WalletToWebRatesEntity ratesEntity = walletToWebRatesDao.findRates();
		    if (ratesEntity != null) {
		        handlingRates = ratesEntity.getHandlingRates() != null 
		            ? BigDecimal.valueOf(ratesEntity.getHandlingRates()) 
		            : BigDecimal.valueOf(WalletToWebRatesEnums.HANDLINGRATES.getValue());
		        
		        serverAmount = ratesEntity.getServerAmount() != null 
		            ? BigDecimal.valueOf(ratesEntity.getServerAmount()) 
		            : BigDecimal.valueOf(WalletToWebRatesEnums.SERVERAMOUNT.getValue());
		        minAmount = ratesEntity.getMinAmount();
		        maxAmount = ratesEntity.getMaxAmount();
		    } else {
		        handlingRates = BigDecimal.valueOf(WalletToWebRatesEnums.HANDLINGRATES.getValue());
		        serverAmount = BigDecimal.valueOf(WalletToWebRatesEnums.SERVERAMOUNT.getValue());
		        minAmount = WalletToWebRatesEnums.MIN_AMOUNT.getValue();
		        maxAmount = WalletToWebRatesEnums.MAX_AMOUNT.getValue();
		    }
		    if (entity.getTransferAmount() < minAmount) {
		        return setResultError(I18nUtil.getMessage("wallet_to_web_min"));
		    }
		    if (entity.getTransferAmount() > maxAmount) {
		        return setResultError(I18nUtil.getMessage("wallet_to_web_max"));
		    }
	        
	        // 计算手续费 = 转账金额 × 费率
	        BigDecimal fee = transferAmount.multiply(handlingRates)
	            .setScale(2, RoundingMode.HALF_UP);
	        
	        // 计算实际到账金额 = 转账金额 - 手续费 - 服务费
	        BigDecimal realAmount = transferAmount.subtract(fee).subtract(serverAmount)
	            .setScale(2, RoundingMode.HALF_UP);
	        
	        // 钱包余额是否足够校验（转为BigDecimal比较）
	        BigDecimal walletBalance = BigDecimal.valueOf(accountEntity.getWalletBalance());
	        if (walletBalance.compareTo(transferAmount) < 0) {
	            return setResultError(I18nUtil.getMessage("wallet_Balance_null"));
	        }
	        
	        // 生成订单号
	        String orderNumber = OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid()));
	        
	        // 创建申请记录（存储时转回Double）
	        entity.setApplyState(ApplicationRecordStateEnums.UNDER_REVIEW.getIndex());
	        entity.setApplyStateName(ApplicationRecordStateEnums.UNDER_REVIEW.getName());
	        entity.setOrderNo(orderNumber);
	        entity.setUserEmail(accountEntity.getUserEmail());
	        entity.setUserTel(accountEntity.getMobileNumber());
	        entity.setServerAmount(serverAmount.doubleValue());      // BigDecimal → Double
	        entity.setHandlingFee(fee.doubleValue());                // BigDecimal → Double
	        entity.setRealAmount(realAmount.doubleValue());          // BigDecimal → Double
	        GenericityUtil.setDate(entity);
	        walletToWebLogDao.insert(entity);
	        
	        // 创建钱包交易记录
	        WalletLogEntity walletLogEntity = new WalletLogEntity();
	        walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid())));
	        walletLogEntity.setOutOrderNo(orderNumber);
	        walletLogEntity.setUserId(accountEntity.getId());
	        walletLogEntity.setUid(accountEntity.getUid());
	        walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
	        walletLogEntity.setTitle(WalletTitleTypeEnums.TRANSFER_OUT_WEB.getCnName());
	        walletLogEntity.setPrimevalMoney(accountEntity.getWalletBalance());
	        walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
	        walletLogEntity.setRealMoney(realAmount);                 // 直接设置BigDecimal
	        walletLogEntity.setServiceCharge(fee.add(serverAmount));  // 手续费+服务费
	        walletLogEntity.setFormName(accountEntity.getUserEmail());
	        walletLogEntity.setFormAccount(accountEntity.getUid());
	        walletLogEntity.setToName(entity.getWalletAddress());
	        walletLogEntity.setToAccount(entity.getWalletAddress());
	        walletLogEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
	        walletLogEntity.setOperateType(WalletTitleTypeEnums.TRANSFER_OUT_WEB.getIndex());
	        walletLogEntity.setNetworkType(entity.getNetworkType());
	        GenericityUtil.setDate(walletLogEntity);
	        walletLogDao.insert(walletLogEntity);
	        
	        // 先行扣除用户钱包余额（用BigDecimal计算后转回Double）
	        BigDecimal newWalletBalance = walletBalance.subtract(transferAmount);
	        accountEntity.setWalletBalance(newWalletBalance.doubleValue());
	        appUserDao.updateById(accountEntity);
	        
	        //发送机器人提现审核消息
			messageTemplateServiceImpl.robotSendAsync(
					MessageEnums.ROBOT_WITHDRAWAL_REVIEW_ZH.getIndex(), 
					LanguageEnums.ZH_CN.getName(), 
					RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
					accountEntity.getUserEmail());
	        
	        return setResultSuccess(I18nUtil.getMessage("reflect_apply_msg"));
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException();
	    }
	}

	@Override
	public ResponseBase pass(Integer id,Integer gooleCode,HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
			String username = token.getName();
			SysUserEntity sysUserEntity = sysUserDao.findByAcctiveState(username, UserStateEnums.NORMAL.getIndex());
			if(sysUserEntity == null) {
				return setResultError("没有操作权限");
			}
			if(gooleCode == null) {
				return setResultError("谷歌验证码不能为空");
			}
			if(sysUserEntity.getGoogleSecretkey() == null) {
				return setResultError("请配置谷歌密钥");
			}
			if(!GoogleAuthenticatorUtil.verifyCode(sysUserEntity.getGoogleSecretkey(), gooleCode)) {
				return setResultError("谷歌验证码错误");
			}
			WalletToWebLogEntity entity = walletToWebLogDao.selectById(id);
			if (entity != null) {
				WithdrawReqEntity reqEntity = new WithdrawReqEntity();
				reqEntity.setChain(entity.getNetworkType());
				reqEntity.setOrderNo(entity.getOrderNo());
				reqEntity.setAmount(entity.getRealAmount().toString());
				reqEntity.setCoin(entity.getTokenName());
				reqEntity.setUid(entity.getUid());
				reqEntity.setAddress(entity.getWalletAddress());
				
				//调用三方提交提现申请
				ResponseBase resData = MonitorUtil.walletPost(UsdtTopinMethods.WITHDRAWAL, reqEntity);
				if (Constants.HTTP_RES_CODE_200.equals(resData.getRtncode())) {
					// 修改提现记录状态
					entity.setApplyState(ApplicationRecordStateEnums.AUDIT_PASS_PROCESSING.getIndex());
					entity.setApplyStateName(ApplicationRecordStateEnums.AUDIT_PASS_PROCESSING.getName());
					walletToWebLogDao.updateById(entity);
					// 修改钱包记录状态
					WalletLogEntity logEntity = walletLogDao.findOutOrderNo(entity.getOrderNo());
					logEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
					walletLogDao.updateById(logEntity);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}
				return resData;
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase reject(Integer id,String rejectContent) {
		try {
			WalletToWebLogEntity entity = walletToWebLogDao.selectById(id);
			if (entity != null) {
				AppUserEntity accountEntity = appUserDao.findByUid(entity.getUid());
				if (accountEntity == null) {
					return setResultError(I18nUtil.getMessage("user.not_null"));
				}
				// 修改提现记录状态
				entity.setApplyState(ApplicationRecordStateEnums.AUDIT_FAIL.getIndex());
				entity.setApplyStateName(ApplicationRecordStateEnums.AUDIT_FAIL.getName());
				entity.setRejectContent(rejectContent);
				walletToWebLogDao.updateById(entity);
				// 修改钱包记录状态
				WalletLogEntity logEntity = walletLogDao.findOutOrderNo(entity.getOrderNo());
				logEntity.setStatus(OrderStateEnums.FAILED.getIndex().toString());
				walletLogDao.updateById(logEntity);
				//新增提现失败退回记录
				this.addWalletLog(logEntity);
				// 金额退回用户钱包
				accountEntity.setWalletBalance(accountEntity.getWalletBalance() + entity.getTransferAmount());
				appUserDao.updateById(accountEntity);
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.WALLET_WITHDRAWAL_ERROR_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						entity.getUid(), 
						entity.getWalletAddress(),
						rejectContent);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void addWalletLog(WalletLogEntity logEntity) {
		try {
			// 创建钱包交易记录
			logEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(logEntity.getUid())));
			logEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(logEntity.getUid())));
			logEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
			logEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			logEntity.setTitle(WalletTitleTypeEnums.TRANSFER_OUT_WEB_RETURN.getCnName());
			logEntity.setOperateType(WalletTitleTypeEnums.TRANSFER_OUT_WEB_RETURN.getIndex());
			GenericityUtil.setDate(logEntity);
			walletLogDao.insert(logEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findRates() {
		try {
			WalletToWebRatesEntity ratesEntity = walletToWebRatesDao.findRates();
			if (ratesEntity == null) {
				WalletToWebRatesEntity entity = new WalletToWebRatesEntity();
				entity.setServerAmount(WalletToWebRatesEnums.SERVERAMOUNT.getValue());
				entity.setHandlingRates(WalletToWebRatesEnums.HANDLINGRATES.getValue());
				entity.setMaxAmount(WalletToWebRatesEnums.MAX_AMOUNT.getValue());
				entity.setMinAmount(WalletToWebRatesEnums.MIN_AMOUNT.getValue());
				return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
			}
			return setResultSuccess(ratesEntity, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody WalletToWebLogEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<WalletToWebLogEntity> list = walletToWebLogDao.findList(entity);
			PageInfo<WalletToWebLogEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase calculateAmount(BigDecimal amount) {
	    // 参数校验
	    if (amount == null) {
	        return setResultError(I18nUtil.getMessage("wallet_to_web_amount_null"));
	    }
	    // 获取费率配置
	    BigDecimal handlingRates = null;
	    BigDecimal serverAmount = null;
	    Double minAmount;
	    Double maxAmount;
	    WalletToWebRatesEntity ratesEntity = walletToWebRatesDao.findRates();
	    if (ratesEntity != null) {
	        handlingRates = ratesEntity.getHandlingRates() != null 
	            ? BigDecimal.valueOf(ratesEntity.getHandlingRates()) 
	            : BigDecimal.valueOf(WalletToWebRatesEnums.HANDLINGRATES.getValue());
	        
	        serverAmount = ratesEntity.getServerAmount() != null 
	            ? BigDecimal.valueOf(ratesEntity.getServerAmount()) 
	            : BigDecimal.valueOf(WalletToWebRatesEnums.SERVERAMOUNT.getValue());
	        minAmount = ratesEntity.getMinAmount();
	        maxAmount = ratesEntity.getMaxAmount();
	    } else {
	        handlingRates = BigDecimal.valueOf(WalletToWebRatesEnums.HANDLINGRATES.getValue());
	        serverAmount = BigDecimal.valueOf(WalletToWebRatesEnums.SERVERAMOUNT.getValue());
	        minAmount = WalletToWebRatesEnums.MIN_AMOUNT.getValue();
	        maxAmount = WalletToWebRatesEnums.MAX_AMOUNT.getValue();
	    }
	    if (amount.doubleValue() < minAmount) {
	        return setResultError(I18nUtil.getMessage("wallet_to_web_min"));
	    }
	    
	    if (amount.doubleValue() > maxAmount) {
	        return setResultError(I18nUtil.getMessage("wallet_to_web_max"));
	    }
	    // 计算手续费 = 金额 × 费率
	    BigDecimal fee = amount.multiply(handlingRates)
	        .setScale(2, RoundingMode.HALF_UP);
	    
	    // 计算实际金额 = 金额 - 手续费 - 服务费
	    BigDecimal realAmount = amount.subtract(fee).subtract(serverAmount)
	        .setScale(2, RoundingMode.HALF_UP);
	    
	    // 确保实际金额不为负数
	    if (realAmount.compareTo(BigDecimal.ZERO) < 0) {
	        return setResultError("扣除手续费后金额为负数，请检查配置");
	    }
	    WalletToWebResEntity entity = new WalletToWebResEntity();
	    entity.setFee(fee);
	    entity.setHandlingRates(handlingRates);
	    entity.setMaxAmount(maxAmount);
	    entity.setMinAmount(minAmount);
	    entity.setOriginalAmount(amount);
	    entity.setRealAmount(realAmount);
	    entity.setServerAmount(serverAmount);
	    return setResultSuccess(entity);
	}
	
	
}
