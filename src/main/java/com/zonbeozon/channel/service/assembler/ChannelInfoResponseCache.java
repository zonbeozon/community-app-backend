package com.zonbeozon.channel.service.assembler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zonbeozon.channel.dto.ChannelInfoResponse;

import com.zonbeozon.channel.repository.ChannelRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * cache 확인 후 없다면 database에서 DTO 생성
 */
public class ChannelInfoResponseCache extends SimpleChannelInfoAssembler implements ChannelInfoAssembler {
    private final Cache<Long, ChannelInfoResponse> responseCache;

    public ChannelInfoResponseCache(Properties cacheProperties, ChannelRepository channelRepository) {
        super(channelRepository);
        responseCache = Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.expireAfterWriteMinutes, TimeUnit.MINUTES)
                .maximumSize(cacheProperties.maximumSize)
                .build();
    }

    @Override
    public ChannelInfoResponse getChannelInfo(Long channelId) {
        ChannelInfoResponse responseFromCache = responseCache.getIfPresent(channelId);
        if (responseFromCache == null) {
            ChannelInfoResponse responseFromDb = super.getChannelInfo(channelId);
            responseCache.put(channelId, responseFromDb);
            return responseFromDb;
        }
        return responseFromCache;
    }

    @Override
    public List<ChannelInfoResponse> getChannelInfos(List<Long> channelIds) {
        Map<Long, ChannelInfoResponse> cachedInfos = responseCache.getAllPresent(channelIds);

        List<Long> notCachedIds = channelIds.stream()
                .filter(id -> !cachedInfos.containsKey(id))
                .toList();

        if (!notCachedIds.isEmpty()) {
            Map<Long, ChannelInfoResponse> newInfosMap = super.getChannelInfos(notCachedIds).stream()
                    .collect(Collectors.toMap(ChannelInfoResponse::channelId, Function.identity()));
            responseCache.putAll(newInfosMap);

            //기존 map과 병합
            cachedInfos.putAll(newInfosMap);
        }

        return channelIds.stream()
                .map(cachedInfos::get)
                .toList();
    }

    @ConfigurationProperties(prefix = "app.cache.channel-info-response")
    public record Properties(
            boolean enabled,
            long expireAfterWriteMinutes,
            long maximumSize
    ) {
    }
}
