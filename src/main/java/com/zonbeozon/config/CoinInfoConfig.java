package com.zonbeozon.config;

import com.zonbeozon.config.properties.CMCApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CMCApiProperties.class)
public class CoinInfoConfig {
}
