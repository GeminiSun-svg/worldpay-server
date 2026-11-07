package com.worldpay.service.impl;

import com.worldpay.utils.api.WalletApi;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.api.request.RobotNotifyEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.UsdtTopinMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.template.EmailTemplateDao;
import com.worldpay.dao.wallet.WalletNetworkTopUpDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.system.EmailTemplateEntity;
import com.worldpay.entity.wallet.WalletNetworkTopUpEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.RobotRoleEnums;
import com.worldpay.enums.UserStateEnums;
import com.worldpay.service.WalletNetworkTopUpService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.MessageFormatUtils;
import com.worldpay.utils.OrderCodeFactory;

@Service
@Transactional
@CrossOrigin
public class WalletNetworkTopUpServiceImpl extends BaseApiService implements WalletNetworkTopUpService{
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
	
	@Autowired
	private WalletNetworkTopUpDao walletNetworkTopUpDao;
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;

	@Override
	public ResponseBase add(@RequestBody WalletNetworkTopUpEntity entity) {
		try {
			AppUserEntity appUser = appUserDao.findByUid(entity.getUid());
			if(appUser == null) {
				return setResultError(I18nUtil.getMessage("user.account_error"));
			}
			if(UserStateEnums.LOGOUT.getIndex().equals(appUser.getUserState())) {
				return setResultError(I18nUtil.getMessage("user.account_null"));
			}
			String orderNo = OrderCodeFactory.getOrderCode(Long.parseLong(appUser.getUid()));
			entity.setUserEmail(appUser.getUserEmail());
			entity.setUserTel(appUser.getMobileNumber());
			entity.setOrderNo(orderNo);
			entity.setOrderState(OrderStateEnums.PENDING_PROCESSING.getIndex());
			entity.setOrderStateName(OrderStateEnums.PENDING_PROCESSING.getZhName());
			GenericityUtil.setDate(entity);
			walletNetworkTopUpDao.insert(entity);
			//发送飞机消息
			EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(MessageEnums.WALLET_NETWORK_TOPUP_ZH.getIndex(), LanguageEnums.ZH_CN.getName());
			if(templateEntity != null) {
				String htmlContent = MessageFormatUtils.format(templateEntity.getTemplateContent(),
						appUser.getUserEmail(), appUser.getUid(),entity.getNetworkType(),orderNo);
				RobotNotifyEntity notifyEntity = new RobotNotifyEntity();
				notifyEntity.setType(RobotRoleEnums.FINANCE.getIndex());
				notifyEntity.setContent(htmlContent);
				try {
					WalletApi.walletPost(UsdtTopinMethods.MSG_NOTIFY, notifyEntity);
				} catch (Exception e) {
					return setResultSuccess(I18nUtil.getMessage("base_success")); 
				}
			}
			return setResultSuccess(I18nUtil.getMessage("base_success")); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody WalletNetworkTopUpEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<WalletNetworkTopUpEntity> list = walletNetworkTopUpDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					AppUserEntity userEntity = appUserDao.findByUid(list.get(i).getUid());
					if(userEntity != null) {
						list.get(i).setWalletBalance(userEntity.getWalletBalance());
					}
				}
			}
			PageInfo<WalletNetworkTopUpEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success")); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase close(Integer id, String reviewRemark) {
		try {
			WalletNetworkTopUpEntity entity = walletNetworkTopUpDao.selectById(id);
			if(entity != null) {
				entity.setReviewRemark(reviewRemark);
				entity.setOrderState(OrderStateEnums.FAILED.getIndex());
				entity.setOrderStateName(OrderStateEnums.FAILED.getZhName());
				walletNetworkTopUpDao.updateById(entity);
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.WALLET_TOPUP_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						entity.getUid(), 
						reviewRemark);
				
				return setResultSuccess(I18nUtil.getMessage("base_success")); 
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
