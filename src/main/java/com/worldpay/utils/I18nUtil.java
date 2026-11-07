package com.worldpay.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.worldpay.config.LanguageContext;

import java.util.Locale;

@Component
public class I18nUtil {

	private static MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		I18nUtil.messageSource = messageSource;
	}

	/**
	 * 获取国际化消息
	 * 
	 * @param code 配置文件中的 key
	 * @param args 占位符参数，可选
	 */
	public static String getMessage(String code, Object... args) {
		String lang = LanguageContext.getLanguage();
		Locale locale;
		switch (lang) {
		case "tw": // 中文繁体
			locale = Locale.TAIWAN;
			break;
		case "zh": // 中文简体
			locale = Locale.SIMPLIFIED_CHINESE;
			break;
		case "en": // 英文
		default:
			locale = Locale.ENGLISH;
		}
		return messageSource.getMessage(code, args, locale);
	}

}
