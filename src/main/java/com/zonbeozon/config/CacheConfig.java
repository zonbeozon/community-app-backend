package com.zonbeozon.config;

import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.service.assembler.ChannelInfoAssembler;
import com.zonbeozon.channel.service.assembler.ChannelInfoResponseCache;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(ChannelInfoResponseCache.Properties.class)
@RequiredArgsConstructor
public class CacheConfig {
    private final ChannelRepository channelRepository;
    @Bean
    @ConditionalOnProperty(
            prefix = "app.cache.channel-info-response",
            name = "enabled",
            havingValue = "true"
    )
    @Primary
    public ChannelInfoAssembler cacheEnabledChannelInfoAssembler(
            ChannelInfoResponseCache.Properties properties
    ) {
        return new ChannelInfoResponseCache(properties, channelRepository);
    }
}
