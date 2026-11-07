package com.worldpay.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.api.request.IosPayReqEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.goods.IosGoodsDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.goods.IosGoodsEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.service.IosGoodsService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class IosGoodsServiceImpl extends BaseApiService implements IosGoodsService{
	
	@Autowired
	private IosGoodsDao iosGoodsDao;
	
	@Autowired
	private WalletLogDao walletLogDao;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private AppleVerifyServiceImpl appleVerifyServiceImpl;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	

	@Override
	public ResponseBase findAll() {
		List<IosGoodsEntity> list = iosGoodsDao.selectList(null);
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase findById(Integer id) {
		IosGoodsEntity entity = iosGoodsDao.selectById(id);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase findByNum(String num) {
		IosGoodsEntity entity = iosGoodsDao.findByNum(num);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase verify(IosPayReqEntity entity) {
		try {
			 // 1️⃣ 基础参数校验
		    if (StringUtils.isAnyBlank(
		            entity.getUid(),
		            entity.getGoodsNum(),
		            entity.getReceiptData())) {
		        return setResult(Constants.HTTP_RES_CODE_400, I18nUtil.getMessage("parameter_error"), null);
		    }
		    
		    AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
		    if(userEntity == null) {
		    	return setResultError(I18nUtil.getMessage("user.not_null"));
		    }

		    // 2️⃣ 调 Apple 校验
		    JSONObject appleResult= appleVerifyServiceImpl.verifyReceipt(entity.getReceiptData());
		    // 3️⃣ 校验 Apple 返回状态
		    int status = appleResult.getIntValue("status");
		    if (status != 0) {
		    	return setResultError(I18nUtil.getMessage("apple_verify_error"));
		    }

		    // 4️⃣ 解析 receipt
		    JSONObject receipt = appleResult.getJSONObject("receipt");
		    JSONArray inApps = receipt.getJSONArray("in_app");

		    if (inApps == null || inApps.isEmpty()) {
		    	return setResultError(I18nUtil.getMessage("not_pay_log"));
		    }

		    // 5️⃣ 取最近一次交易
		    JSONObject lastPay = inApps.getJSONObject(inApps.size() - 1);
		    String transactionId = lastPay.getString("transaction_id");
		    String productId = lastPay.getString("product_id");

		    // 6️⃣ 校验商品是否一致
		    if (!entity.getGoodsNum().equals(productId)) {
		    	return setResultError(I18nUtil.getMessage("goods_mismatch"));
		    }

//		    // 7️⃣ 防止重复订单（非常关键）
		    WalletLogEntity walletLogEntity = walletLogDao.findOutOrderNo(transactionId);
		    if(walletLogEntity != null) {
		    	return setResultError(I18nUtil.getMessage("order_processed"));
		    }
		    //创建新订单(修改用户钱包余额)
		    this.saveWalletLog(transactionId,userEntity,entity.getGoodsNum());
		    //修改用户
		    // 8️⃣ 返回校验通过结果
		    JSONObject result = new JSONObject();
		    result.put("transactionId", transactionId);
		    result.put("productId", productId);
		    return setResultSuccess(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void saveWalletLog(String transactionId,AppUserEntity userEntity, String goodsNum) {
		try {
			IosGoodsEntity goodsEntity = iosGoodsDao.findByNum(goodsNum);
			AppUserEntity appUserEntity = appUserDao.findByUid(userEntity.getUid());
			if(appUserEntity != null && goodsEntity != null) {
				//修改用户钱包余额
				appUserEntity.setWalletBalance(appUserEntity.getWalletBalance() + Double.parseDouble(goodsEntity.getGoodsName()));
				appUserDao.updateById(appUserEntity);
				//新增记录
				WalletLogEntity walletLogEntity = new WalletLogEntity();
				walletLogEntity.setOrderNo(transactionId);
				walletLogEntity.setOutOrderNo(transactionId);
				walletLogEntity.setUserId(userEntity.getId());
				walletLogEntity.setUid(userEntity.getUid());
				walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
				walletLogEntity.setTitle(WalletTitleTypeEnums.WALLET_TOP_UP.getCnName());
				walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
				walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
				walletLogEntity.setRealMoney(BigDecimal.valueOf(Double.parseDouble(goodsEntity.getGoodsName())));//实际到账金额
				walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));//手续费
				walletLogEntity.setFormName(userEntity.getUserEmail());
				walletLogEntity.setFormAccount(userEntity.getUserEmail());
				walletLogEntity.setToName(userEntity.getUserEmail());
				walletLogEntity.setToAccount(userEntity.getUserEmail());
				walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
				walletLogEntity.setOperateType(WalletTitleTypeEnums.WALLET_TOP_UP.getIndex());
				GenericityUtil.setDate(walletLogEntity);
				walletLogDao.insert(walletLogEntity);
				
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.WALLET_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						userEntity.getUid(), 
						""+goodsEntity.getGoodsName()+TransCurrencyEnums.USD.getName(),
						userEntity.getWalletBalance()+TransCurrencyEnums.USD.getName(),
						userEntity.getFreezeBalance()+TransCurrencyEnums.USD.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase add(IosGoodsEntity entity) {
		try {
			IosGoodsEntity goodsEntity = iosGoodsDao.findByNum(entity.getGoodsNum());
			if(goodsEntity != null) {
				return setResultError("此商品编号已存在");
			}
			GenericityUtil.setDate(entity);
			iosGoodsDao.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(IosGoodsEntity entity) {
		try {
			IosGoodsEntity goodsEntity = iosGoodsDao.selectById(entity.getId());
			if(goodsEntity != null) {
				iosGoodsDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(Integer id) {
		try {
			IosGoodsEntity goodsEntity = iosGoodsDao.selectById(id);
			if(goodsEntity != null) {
				iosGoodsDao.deleteById(id);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(IosGoodsEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<IosGoodsEntity> list = iosGoodsDao.findList(entity);
		// 获取总记录数
		PageInfo<IosGoodsEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}
	
	
}
