package com.worldpay.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.worldpay.constants.Constants;
import com.worldpay.security.CustomAuthenticationProvider;
import com.worldpay.security.GrantedAuthorityImpl;
import com.worldpay.utils.CustomUtils;
import com.worldpay.utils.JsonData;
import com.worldpay.utils.RedisUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT身份验证过滤器 token的校验
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	private RedisUtil redisUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil) {
		super(authenticationManager);
		this.redisUtil = redisUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			// 语言统一由 LanguageInterceptor 处理，此处不再重复设置
			String uri = request.getServletPath();
			if (releaseApi(uri)) {
				chain.doFilter(request, response);
				return;
			}

			UsernamePasswordAuthenticationToken authentication = null;
			try {
				authentication = getAuthentication(request);
			} catch (Exception e) {
				CustomUtils.sendJsonMessage(response, JsonData.buildError("token不合法"));
			}
			String header = request.getHeader(Constants.HEADER_AUTH);
			String requestURI = request.getServletPath();
			if (header == null || !header.startsWith(Constants.AUTH_HEADER_START_WITH)
					|| requestURI.equals("/oauth/login")) {
				chain.doFilter(request, response);
				return;
			}
			if (authentication == null) {
				CustomUtils.sendJsonMessage(response, JsonData.buildError("登录已过期"));
			} else {
				String key = authentication.getName();
				String redisKey = Constants.APP_PACKAGE_NAME + key;
				if (redisUtil.get(redisKey) == null || redisUtil.get(redisKey).toString().length() < 1) {
					chain.doFilter(request, response);
					return;
				} else {
					if (redisUtil.get(redisKey).equals(header)) {
						SecurityContextHolder.getContext().setAuthentication(authentication);
						chain.doFilter(request, response);
					} else {
						CustomUtils.sendJsonMessage(response, JsonData.buildError("登录已过期"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			CustomUtils.sendJsonMessage(response, JsonData.Error("系统异常稍后再试"));
		}
	}

	public static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(Constants.HEADER_AUTH);
		String uri = request.getServletPath();
		boolean temp = releaseApi(uri);
		if (token != null && !temp) {
			try {
				String userAndRole = Jwts.parser()
						.verifyWith(Constants.jwtKey())
						.build()
						.parseSignedClaims(token.replace(Constants.AUTH_HEADER_START_WITH, "")).getPayload().getSubject();
				String user = "";
				List<GrantedAuthorityImpl> menuCodeList = new ArrayList<>();
				if (userAndRole != null) {
					String[] userAndMenus = userAndRole.split(",");
					int length = userAndMenus.length;
					if (length > 0) {
						user = userAndMenus[0];
					}
					menuCodeList = CustomAuthenticationProvider.getUserAuthorities(user);
					return new UsernamePasswordAuthenticationToken(user, null, menuCodeList);
				}
				return null;
			} catch (ExpiredJwtException e) {
				throw new RuntimeException("登录已过期，请重新登录");
			}
		}
		return null;
	}

	public static boolean releaseApi(String uri) {
		if (uri.startsWith("/card/api/app-user/sign-up")
				|| uri.startsWith("/card/api/app-user/login")
				|| uri.startsWith("/card/api/app-user/google-code")
				|| uri.startsWith("/card/api/app-user/send-email-code")
				|| uri.startsWith("/card/api/app-user/email-code")
				|| uri.startsWith("/card/api/app-user/google-secretkey")
				|| uri.startsWith("/card/api/app-user-sys/forget-passwrod")
				|| uri.startsWith("/card/api/wallet/topin-usdt-notify")
				|| uri.startsWith("/card/api/webhook")
				|| uri.startsWith("/card/api/info/config-info")
				|| uri.startsWith("/card/api/email-template/send-test")
				|| uri.startsWith("/card/api/sign")) {
			return true;
		}
		return false;
	}

}
