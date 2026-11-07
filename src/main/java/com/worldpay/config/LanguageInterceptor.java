package com.worldpay.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 语言上下文拦截器
 *
 * 支持 Header 传入语言: lang=zh/en/zh_TW
 * 默认从 Accept-Language 获取
 */
@Component
public class LanguageInterceptor implements HandlerInterceptor {

    private static final String LANG_HEADER = "lang";
    private static final String LANG_CONTEXT_KEY = "language";
    public static final String DEFAULT_LANG = "en";

    @Resource
    private jakarta.servlet.http.HttpServletRequest currentRequest;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getHeader(LANG_HEADER);
        if (lang == null || lang.isEmpty()) {
            lang = request.getParameter("lang");
        }
        if (lang == null || lang.isEmpty()) {
            String acceptLang = request.getHeader("Accept-Language");
            if (acceptLang != null && acceptLang.contains("zh-TW")) {
                lang = "zh_TW";
            } else if (acceptLang != null && acceptLang.startsWith("zh")) {
                lang = "zh";
            } else {
                lang = DEFAULT_LANG;
            }
        }
        LanguageContext.setLanguage(lang);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LanguageContext.clear();
    }
}
