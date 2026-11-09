package com.worldpay.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

/**
 * 语言上下文拦截器
 *
 * 语言来源优先级：Header lang > Header language（兼容旧客户端）> URL 参数 lang > Accept-Language
 * 统一归一化为 zh / en / tw，默认 en
 */
@Component
public class LanguageInterceptor implements HandlerInterceptor {

    private static final String LANG_HEADER = "lang";
    private static final String LANG_HEADER_LEGACY = "language";
    public static final String DEFAULT_LANG = "en";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getHeader(LANG_HEADER);
        if (isBlank(lang)) {
            lang = request.getHeader(LANG_HEADER_LEGACY);
        }
        if (isBlank(lang)) {
            lang = request.getParameter(LANG_HEADER);
        }
        if (isBlank(lang)) {
            lang = request.getHeader("Accept-Language");
        }
        LanguageContext.setLanguage(normalize(lang));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LanguageContext.clear();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 归一化语言标识：tw / zh-TW / zh-HK -> tw，zh / zh-CN -> zh，en-* -> en，其余 -> en
     */
    private static String normalize(String lang) {
        if (isBlank(lang)) {
            return DEFAULT_LANG;
        }
        String value = lang.trim().toLowerCase(Locale.ROOT).replace('-', '_');
        if (value.equals("tw") || value.startsWith("zh_tw") || value.startsWith("zh_hk")) {
            return "tw";
        }
        if (value.startsWith("zh")) {
            return "zh";
        }
        if (value.startsWith("en")) {
            return "en";
        }
        return DEFAULT_LANG;
    }
}
