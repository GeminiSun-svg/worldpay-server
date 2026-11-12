package com.worldpay;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.micrometer.core.instrument.MeterRegistry;


@SpringBootApplication
@MapperScan("com.worldpay.dao")
@EnableTransactionManagement
@EnableScheduling
//@EnableDiscoveryClient//nacos
public class WorldPayApplication {
	
    public static void main(String[] args) {
    	SpringApplication.run(WorldPayApplication.class, args);
    }
    
    //避免请求绕过安全检查,url 存在//问题
    @Bean
    public HttpFirewall httpFirewall() { 
        return new DefaultHttpFirewall();
    }
    
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name:}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }

    
}
