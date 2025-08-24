package com.zonbeozon.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.service.cache.ChannelCacheInvalidator;
import com.zonbeozon.channel.service.assembler.ChannelInfoAssembler;
import com.zonbeozon.channel.service.assembler.CacheEnabledChannelInfoAssembler;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(CacheConfig.ChannelInfoResponseCacheProperties.class)
@EnableCaching
@Profile("cache")
public class CacheConfig {
    @Bean
    @ConditionalOnProperty(
            prefix = "app.cache.channel-info-response",
            name = "enabled",
            havingValue = "true"
    )
    @Primary
    public ChannelInfoAssembler cacheEnabledChannelInfoAssembler(
            ChannelInfoResponseCacheProperties properties,
            CacheManager cacheManager,
            ChannelRepository channelRepository
    ) {
        return new CacheEnabledChannelInfoAssembler(cacheManager.getCache(properties.name()), channelRepository);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "app.cache.channel-info-response",
            name = "enabled",
            havingValue = "true"
    )
    public ChannelCacheInvalidator channelInfoResponseCacheInvalidator(
            ChannelInfoResponseCacheProperties properties,
            CacheManager cacheManager
    ) {
        Cache cache = cacheManager.getCache(properties.name());
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + properties.name());
        }
        return cache::evict;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "app.cache.channel-info-response",
            name = "enabled",
            havingValue = "true"
    )
    public CacheManagerCustomizer<CaffeineCacheManager> channelInfoCacheCustomizer(
            ChannelInfoResponseCacheProperties properties
    ) {
        return cacheManager -> cacheManager.registerCustomCache(
                properties.name(),
                Caffeine.newBuilder()
                        .expireAfterWrite(properties.expireAfterWriteMinutes(), TimeUnit.MINUTES)
                        .maximumSize(properties.maximumSize())
                        .build()
        );
    }

    @ConfigurationProperties(prefix = "app.cache.channel-info-response")
    public record ChannelInfoResponseCacheProperties(
            boolean enabled,
            String name,
            long expireAfterWriteMinutes,
            long maximumSize
    ) {
    }
}
