package com.worldpay.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 配置
 * 替代旧版 Swagger Bootstrap UI
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI worldPayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WorldPay 2.0 API")
                        .description("WorldPay 全球数字卡支付平台后端 API")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("WorldPay Team")
                                .email("support@worldpay.club")))
                .addSecurityItem(new SecurityRequirement().addList("token"))
                .schemaRequirement("token", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("token")
                        .description("JWT Token"));
    }
}
