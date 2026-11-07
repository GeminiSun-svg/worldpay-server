package com.worldpay.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.stereotype.Service;

import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.goods.IosGoodsEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.query.ApplyPayRequest;
import com.worldpay.service.ApplyPayService;
import com.worldpay.utils.GenericityUtil;

@Service
@Transactional
@CrossOrigin
public class ApplyPayServiceImpl extends BaseApiService implements ApplyPayService {


	@Autowired
	private WalletLogDao walletLogDao;

	@Override
	public ResponseBase createPayment(@RequestAttribute ApplyPayRequest entity) {
		try {
//			// 查询用户信息
//			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
//			if (userEntity == null) {
//				return setResultError(I18nUtil.getMessage("account_error"));
//			}
//			// 查询商品信息
//			IosGoodsEntity goodsEntity = iosGoodsDao.findByNum(entity.getGoodsNum());
//			if (goodsEntity == null) {
//				return setResultError(I18nUtil.getMessage("account_error"));
//			}
//			String orderNum = OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid()));
//			// 创建待支付订单
//			this.saveWalletLog(userEntity, goodsEntity, orderNum);
//			// 向 Stripe 创建 PaymentIntent
//			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//					// 支付金额（单位：分）
//					.setAmount(req.getAmount())
//					// 币种
//					.setCurrency(req.getCurrency())
//					/*
//					 * 开启自动支付方式： - Apple Pay - 信用卡 - Google Pay（如果支持）
//					 *
//					 * Stripe 会自动判断
//					 */
//					.setAutomaticPaymentMethods(
//							PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
//					/*
//					 * metadata 非常重要： - 不参与支付 - 但会原样返回给 Webhook - 用于关联你自己的订单
//					 */
//					.putMetadata("orderId", orderNum).build();
//
//			// 向 Stripe 创建 PaymentIntent
//			PaymentIntent intent = PaymentIntent.create(params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private void saveWalletLog(AppUserEntity userEntity, IosGoodsEntity goodsEntity, String orderNum) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(orderNum);
			walletLogEntity.setOutOrderNo(orderNum);
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
			walletLogEntity.setTitle(WalletTitleTypeEnums.WALLET_TOP_UP.getCnName());
			walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(Double.parseDouble(goodsEntity.getGoodsName())));// 实际到账金额
			walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));// 手续费
			walletLogEntity.setFormName(userEntity.getUserEmail());
			walletLogEntity.setFormAccount(userEntity.getUserEmail());
			walletLogEntity.setToName(userEntity.getUserEmail());
			walletLogEntity.setToAccount(userEntity.getUserEmail());
			walletLogEntity.setStatus(OrderStateEnums.PROCESSING.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.WALLET_TOP_UP.getIndex());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
