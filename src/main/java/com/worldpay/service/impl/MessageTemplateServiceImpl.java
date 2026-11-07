package com.worldpay.service.impl;
import com.worldpay.utils.JPushUtils;
import com.worldpay.utils.api.MonitorUtil;
import com.worldpay.utils.GeTuiPushUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.system.MessageTemplateDao;
import com.worldpay.dao.template.EmailTemplateDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.system.EmailTemplateEntity;
import com.worldpay.entity.system.MessageTemplateEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.service.MessageTemplateService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.MessageFormatUtils;
import com.worldpay.utils.TelegramNotificationUtil;

@Service
@Transactional
public class MessageTemplateServiceImpl extends BaseApiService implements MessageTemplateService{
	
	@Autowired
	private MessageTemplateDao messageTemplateDao;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;
	
	@Autowired
	private EmailTemplateServiceimpl emailTemplateServiceimpl;
	
	@Autowired
	private NoticeServiceImpl noticeServiceImpl;
	
	@Autowired
	private TelegramNotificationUtil telegramNotificationUtil;
	
	@Autowired
	private GeTuiPushUtils pushUtils;

	@Override
	@SysLogAnnotation(module = "消息模板", type = "post", remark = "新增消息模板")
	public ResponseBase add(MessageTemplateEntity entity) {
		try {
			//查询相同语言下code是否已存在
			MessageTemplateEntity template = messageTemplateDao.findOne(entity.getMessageCode(),entity.getLanguage());
			if(template != null) {
				return setResultError(I18nUtil.getMessage("language_inf_exit"));
			}
			entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
			GenericityUtil.setDate(entity);
			messageTemplateDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "消息模板", type = "post", remark = "编辑消息模板")
	public ResponseBase update(MessageTemplateEntity entity) {
		try {
			MessageTemplateEntity template = messageTemplateDao.selectById(entity.getId());
			if(template != null) {
				messageTemplateDao.updateById(entity);
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
	@SysLogAnnotation(module = "消息模板", type = "get", remark = "删除消息模板")
	public ResponseBase delete(Integer id) {
		try {
			MessageTemplateEntity template = messageTemplateDao.selectById(id);
			if(template != null) {
				messageTemplateDao.deleteById(id);
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
	@SysLogAnnotation(module = "消息模板", type = "get", remark = "根据code查询消息模板")
	public MessageTemplateEntity findCode(Integer code) {
		try {
			String language = LanguageContext.getLanguage();
			MessageTemplateEntity template = messageTemplateDao.findOne(code, language);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "消息模板", type = "post", remark = "根据code查询消息模板")
	public ResponseBase findList(MessageTemplateEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<MessageTemplateEntity> list = messageTemplateDao.findList(entity);
		PageInfo<MessageTemplateEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}
	
	/**
	 * @category 统一发送消息
	 * @param templateCode
	 * @param language
	 * @param uid
	 * @param params
	 */
	@Async("asyncExecutor")
	public void unifiedSendAsync(Integer templateCode, String language, String uid, Object... params) {
		System.out.println("异步调用消息发送，站内信新增");
		AppUserEntity entity = appUserDao.findByUid(uid);
		if (entity != null) {
			// 新增站内消息
			noticeServiceImpl.addMsg(entity.getUid(), templateCode, params);
			if(entity.getUserEmail() != null && !entity.getUserEmail().isEmpty()) {
				// 发送邮件
				emailTemplateServiceimpl.send(entity.getUserEmail(), templateCode, language, params);
			}
			if(entity.getCid() != null && !entity.getCid().isEmpty()) {
				// 消息推送
				this.send(templateCode, language, entity.getCid(), params);
			}

		}
	}

	/**
	 * @category 推送消息
	 * @param registrationId
	 * @param templateCode
	 * @param language
	 * @param uid
	 * @param params
	 */
	public void send(Integer templateCode,String language, String cid,Object... params) {
		MessageTemplateEntity template = messageTemplateDao.findOne(templateCode, language);
		if (template == null) {
			return;
		}
		String message = MessageFormatUtils.format(template.getMessageInfo(), params);
//		pushUtils.pushToSingleByCid(cid,template.getMsgTitles(),message, null);
		JPushUtils.sendToDevice(cid, template.getMsgTitles(), message);
	}
	
	/**
	 * @category 飞机消息
	 * @param code
	 * @param language
	 * @param robotType
	 * @param arg
	 */
	@Async("asyncExecutor") // 指定线程池，可省略参数
	public void robotSendAsync(Integer code, String language,Integer robotType, Object... arg) {
	    try {
	        // 调用原有的同步方法
	        this.robotSend(code, language,robotType, arg);
	    } catch (Exception e) {
	        // 捕获异常，防止线程异常终止
	        e.printStackTrace();
	    }
	}
	
	/**
	 * @category 飞机消息
	 * @param code
	 * @param language
	 * @param robotType
	 * @param arg
	 */
	public void robotSend(Integer code, String language,Integer robotType, Object... arg) {
		try {
			EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(code,language);
			if(templateEntity != null) {
				//添加动态数据
				String htmlContent = MessageFormatUtils.format(
						templateEntity.getTemplateContent(),arg);
//				RobotNotifyEntity notifyEntity = new RobotNotifyEntity();
//				notifyEntity.setType(robotType);
//				notifyEntity.setContent(htmlContent);
//				MonitorUtil.walletPost(UsdtTopinMethods.MSG_NOTIFY, notifyEntity);
				String html = MessageFormatUtils.saveTelegramHtml(htmlContent);
				telegramNotificationUtil.sendTelegramBindingMsg(html);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	
	

}
