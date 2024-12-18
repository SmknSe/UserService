package com.example.userservice.config;

import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InetUtilsConfig {

    @Bean
    public InetUtils inetUtils() {
        return new InetUtils(new InetUtilsProperties());
    }
}
