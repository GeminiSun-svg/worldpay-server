package com.worldpay.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.template.EmailTemplateDao;
import com.worldpay.entity.system.EmailTemplateEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.service.EmailTemplateService;
import com.worldpay.utils.EmailUtil;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.MessageFormatUtils;

@Service
@Transactional
@CrossOrigin
public class EmailTemplateServiceimpl extends BaseApiService implements EmailTemplateService{
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;

	@Override
	public ResponseBase add(@RequestBody EmailTemplateEntity entity) {
		try {
			//查询相同语言下code是否已存在
			EmailTemplateEntity template = emailTemplateDao.findByNum(entity.getTemplateNumber(),entity.getLanguage());
			if(template != null) {
				return setResultError(I18nUtil.getMessage("Template_exist"));
			}
			entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
			entity.setTemplateSubject(entity.getTemplateName());
			GenericityUtil.setDate(entity);
			emailTemplateDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody EmailTemplateEntity entity) {
		try {
			EmailTemplateEntity template = emailTemplateDao.selectById(entity.getId());
			if(template != null) {
				entity.setTemplateSubject(entity.getTemplateName());
				emailTemplateDao.updateById(entity);
				return setResultSuccess();
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(Integer id) {
		try {
			EmailTemplateEntity template = emailTemplateDao.selectById(id);
			if(template != null) {
				emailTemplateDao.deleteById(id);
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
	public EmailTemplateEntity findCode(Integer code) {
		try {
			String language = LanguageContext.getLanguage();
			EmailTemplateEntity template = emailTemplateDao.findByNum(code, language);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody EmailTemplateEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<EmailTemplateEntity> list = emailTemplateDao.findList(entity);
		PageInfo<EmailTemplateEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}

	/**
	 * @category 邮件发送
	 * @param userEmail
	 * @param code
	 * @param language
	 * @param arg
	 */
	public void send(String userEmail,Integer code, String language, Object... arg) {
		try {
			EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(code,language);
			if(templateEntity != null) {
				//添加动态数据
				String htmlContent = MessageFormatUtils.format(
						templateEntity.getTemplateContent(),arg);
				String html = MessageFormatUtils.saveHtml(htmlContent, language);
				EmailUtil.sendEmail(userEmail, templateEntity.getTemplateSubject(), html);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category 邮件发送
	 * @param userEmail
	 * @param code
	 * @param language
	 * @param arg
	 */
	@Async("asyncExecutor") // 指定线程池，可省略参数
	public void sendAsync(String userEmail, Integer code, String language, Object... arg) {
	    try {
	        // 调用原有的同步方法
	        this.send(userEmail, code, language, arg);
	    } catch (Exception e) {
	        // 捕获异常，防止线程异常终止
	        e.printStackTrace();
	    }
	}
	
}
