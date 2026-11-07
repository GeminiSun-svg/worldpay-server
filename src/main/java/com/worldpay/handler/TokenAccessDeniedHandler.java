package com.worldpay.handler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.worldpay.utils.CustomUtils;
import com.worldpay.utils.JsonData;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足处理器
 */
public class TokenAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CustomUtils.sendJsonMessage(response, JsonData.PowerError("权限不够，请联系管理员！！！"));
    }


}

