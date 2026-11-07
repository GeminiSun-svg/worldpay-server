package com.worldpay.handler;
import com.worldpay.filter.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.worldpay.utils.CustomUtils;
import com.worldpay.utils.JsonData;
import com.worldpay.utils.RedisUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销处理器
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	UsernamePasswordAuthenticationToken head = JWTAuthenticationFilter.getAuthentication(request);
    	String redisKey = null;
    	if(head == null) {
    		CustomUtils.sendJsonMessage(response, JsonData.buildError("token已过期"));
    	}else {
        	redisKey = head.getName();
    	}
        try {
            if (redisKey == null) {
                CustomUtils.sendJsonMessage(response, JsonData.buildError("未登录，不能进行注销操作！！！"));
            } else {
                if (redisUtil.get(redisKey) == null) {
                    CustomUtils.sendJsonMessage(response, JsonData.buildError("登录凭证异常，注销失败！！！"));
                } else {
                	 //清空token
                    redisUtil.del(redisKey);
                    CustomUtils.sendJsonMessage(response, JsonData.buildSuccess("注销成功"));
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            CustomUtils.sendJsonMessage(response, JsonData.buildError("登录凭证异常，注销失败！！！"));
        }
        CustomUtils.sendJsonMessage(response, JsonData.buildError("登录过期，重新登录"));
    }

}

