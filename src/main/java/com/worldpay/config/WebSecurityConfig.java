package com.worldpay.config;

import com.worldpay.dao.system.IpWhitelistDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.filter.GlobalRateLimitFilter;
import com.worldpay.filter.JWTAuthenticationFilter;
import com.worldpay.filter.JWTLoginFilter;
import com.worldpay.handler.AuthenticationLogout;
import com.worldpay.handler.TokenAccessDeniedHandler;
import com.worldpay.handler.TokenAuthenticationEntryPoint;
import com.worldpay.security.CustomAuthenticationProvider;
import com.worldpay.utils.IpUtil;
import com.worldpay.utils.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类（适配 Spring Security 6）
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IpUtil ipUtil;

    @Autowired
    private AuthenticationLogout authenticationLogout;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private IpWhitelistDao ipWhitelistDao;

    @Autowired
    private GlobalRateLimitFilter globalRateLimitFilter;

    @Value("${dscoins.login.ipLimit.enable:false}")
    private Boolean ipLimit;

    @Value("${dscoins.login.googleLimit.enable:false}")
    private Boolean googleLimit;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, sysUserDao, null));
        http.cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/statistics/**").permitAll()
                        .requestMatchers("/druid/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/card/api/app-user/**").permitAll()
                        .requestMatchers("/card/api/webhook/**").permitAll()
                        .requestMatchers("/card/api/sys-apk/last-version").permitAll()
                        .requestMatchers("/card/api/app-user-sys/forget-passwrod").permitAll()
                        .requestMatchers("/card/api/wallet/topin-usdt-notify").permitAll()
                        .requestMatchers("/card/api/wallet/topin-usdt-address").permitAll()
                        .requestMatchers("/card/api/wallet/withdrawal-notify").permitAll()
                        .requestMatchers("/card/api/info/config-info").permitAll()
                        .requestMatchers("/card/api/email-template/send-test").permitAll()
                        .requestMatchers("/card/api/sign/**").permitAll()
                        .requestMatchers("/card/api/forecast/callback").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/configuration/ui").permitAll()
                        .requestMatchers("/configuration/security").permitAll()
                        .requestMatchers("/*.html", "/*.css", "/*.js", "/webSocket/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessHandler(authenticationLogout)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new TokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new TokenAccessDeniedHandler())
                )
                .addFilterBefore(globalRateLimitFilter, JWTLoginFilter.class)
                .addFilter(new JWTLoginFilter(authenticationManager, redisUtil, sysUserDao, ipWhitelistDao, ipLimit, googleLimit, ipUtil))
                .addFilter(new JWTAuthenticationFilter(authenticationManager, redisUtil));

        return http.build();
    }
}
