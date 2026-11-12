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
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    /**
     * 认证管理器：只使用自定义的 {@link CustomAuthenticationProvider}（MD5 校验）。
     *
     * 不能直接用 {@code config.getAuthenticationManager()}：由于容器里存在
     * UserDetailsService Bean，Spring Security 会自动装配一个 DaoAuthenticationProvider，
     * 该 Provider 默认使用 DelegatingPasswordEncoder，而数据库里存的是无前缀的 MD5 密文，
     * 于是抛 IllegalArgumentException: each password must have a password encoding prefix。
     * 老版本（WebSecurityConfigurerAdapter）是通过 configure(AuthenticationManagerBuilder)
     * 显式注册自定义 Provider 覆盖掉的，这里等价地手动构建 ProviderManager。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return new ProviderManager(new CustomAuthenticationProvider(userDetailsService, sysUserDao, null));
    }

    /**
     * CORS 配置源（供 Spring Security + Spring MVC 共享）
     * 与 WorldPayApplication 里的 CorsFilter 保持一致。
     * 但 Security 的 .cors() 必须用 CorsConfigurationSource，不能用 CorsFilter Bean
     * （二者是两套独立入口，会叠加生效导致 Header 重复），
     * 所以这里统一在 Security filter chain 里配置 CORS，
     * WorldPayApplication 的 CorsFilter Bean 可以删掉。
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("token");
        config.addExposedHeader("Access-Control-Expose-Headers");
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, sysUserDao, null));
        http.cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 放行所有 OPTIONS 预检请求（浏览器跨域先调这个）
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/login").permitAll()
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
