package com.worldpay.scheduled;
import com.worldpay.utils.api.UCardApi;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.product.ProductOrderDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.product.ProductOrderEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.KycApiStateEnums;
import com.worldpay.enums.KycStateEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.PublicEnums;
import com.worldpay.enums.RobotRoleEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.service.impl.MessageTemplateServiceImpl;
import com.worldpay.utils.DateUtil;
import com.worldpay.utils.FinanceUtils;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.OrderCodeFactory;


@Component
public class ScheduledTasks {

	@Autowired
	private ProductOrderDao productOrderDao;

	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	
	@Autowired
	private WalletLogDao walletLogDao;

	/**
	 * @category 查询到期定期产品订单，进行收益计算，钱包解冻，状态修改
	 */
	@Scheduled(cron = "0 0/5 * * * *")//五分钟执行一次
	public void orderFinish() {
		Date now = new Date();
		List<ProductOrderEntity> list = productOrderDao.findExpireOrder(OrderStateEnums.POSTED.getName(),
				PublicEnums.TOW.getIndex(), now);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				AppUserEntity appUserEntity = appUserDao.findByUid(list.get(i).getUid());
				if (appUserEntity != null) {
					// 修改订单内容
					Integer days = DateUtil.getDayDisparityNum(list.get(i).getPayTime(), new Date()); // 持有天数
					Double profit = FinanceUtils.calculateProfit(list.get(i).getOrderMonery(),
							list.get(i).getInterestRate(), days);// 收益
					list.get(i).setActualIncome(profit);
					list.get(i).setOrderState(OrderStateEnums.FINISH.getName());
					list.get(i).setFinishTime(new Date());
					productOrderDao.updateById(list.get(i));
					// 解冻余额，增加收益
					Double balance = appUserEntity.getWalletBalance();
					Double money = list.get(i).getOrderMonery()+ profit;
					appUserEntity.setWalletBalance(appUserEntity.getWalletBalance() +list.get(i).getOrderMonery()+ profit);
					appUserEntity.setFreezeBalance(appUserEntity.getFreezeBalance() - list.get(i).getOrderMonery());
					appUserDao.updateById(appUserEntity);
					//新增钱包交易记录
					this.addWalletLog(appUserEntity, money, balance);
					
				}

			}
		}
	}
	
	
	public void addWalletLog(AppUserEntity userEntity, Double money, Double balance) {
		try {
			WalletLogEntity walletLogEntity = new WalletLogEntity();
			walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
			walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
			walletLogEntity.setUserId(userEntity.getId());
			walletLogEntity.setUid(userEntity.getUid());
			walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
			walletLogEntity.setTitle(WalletTitleTypeEnums.PAY_INVESTMENT_RETURNS.getCnName());
			walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
			walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
			walletLogEntity.setRealMoney(BigDecimal.valueOf(money));
			walletLogEntity.setFormName(userEntity.getUserEmail());
			walletLogEntity.setFormAccount(userEntity.getUserEmail());
			walletLogEntity.setToName(userEntity.getUserEmail());
			walletLogEntity.setToAccount(userEntity.getUserEmail());
			walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
			walletLogEntity.setOperateType(WalletTitleTypeEnums.PAY_INVESTMENT_RETURNS.getIndex());
			GenericityUtil.setDate(walletLogEntity);
			walletLogDao.insert(walletLogEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Scheduled(cron = "0 0/1 * * * *") //1分钟执行一次
	public void getKycState() {
		try {
			//查询数据库kyc认证中的用户
			List<AppUserEntity> list = appUserDao.findKyc(KycStateEnums.PROCESS_APPROVE.getIndex());
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ResponseBase resData = UCardApi.postData(list.get(i).getUid(), UCardMethods.USER_KYC_STATUS, new Object(), null);
					JSONObject jsonObject = JSON.parseObject(resData.getData().toString());
					String status = jsonObject.get("status").toString();
					if(KycApiStateEnums.SUCCESS.getName().equals(status)) {//认证通过
						list.get(i).setKycState(KycStateEnums.SUCCESS_APPROVE.getIndex());
						list.get(i).setKycStateName(KycStateEnums.SUCCESS_APPROVE.getName());
						appUserDao.updateById(list.get(i));
						this.sendKycMsg(MessageEnums.KYC_SUCCESS_ZH.getIndex(),
								list.get(i).getUid(),
								null,list.get(i).getUserEmail());
						
					}else if(KycApiStateEnums.FAIL.getName().equals(status)) {//认证不通过
						list.get(i).setKycState(KycStateEnums.ERROR_APPROVE.getIndex());
						list.get(i).setKycStateName(KycStateEnums.ERROR_APPROVE.getName());
						list.get(i).setKycAuditResult(jsonObject.get("failedReason").toString());
						appUserDao.updateById(list.get(i));
						this.sendKycMsg(MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(),list.get(i).getUid(),jsonObject.get("failedReason").toString(),list.get(i).getUserEmail());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	public void sendKycMsg(Integer msgCode,String uid,String msgInfo,String userEmail) {
		try {
			if(msgInfo != null && msgInfo.length() > 0) {//认证不通过
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						uid,msgInfo);
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
	

}
