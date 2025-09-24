package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoDto;

import com.zonbeozon.channel.repository.ChannelRepository;
import org.springframework.cache.Cache;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * cache 확인 후 없다면 database에서 DTO 생성
 */
public class CacheEnabledChannelInfoAssembler extends SimpleChannelInfoAssembler implements ChannelInfoAssembler {
    private final Cache cache;

    public CacheEnabledChannelInfoAssembler(Cache cache, ChannelRepository channelRepository) {
        super(channelRepository);
        this.cache = cache;
    }

    @Override
    public ChannelInfoDto getChannelInfo(Long channelId) {
        ChannelInfoDto responseFromCache = cache.get(channelId, ChannelInfoDto.class);
        if (responseFromCache == null) {
            ChannelInfoDto responseFromDb = super.getChannelInfo(channelId);
            cache.put(channelId, responseFromDb);
            return responseFromDb;
        }
        return responseFromCache;
    }

    @Override
    public List<ChannelInfoDto> getChannelInfos(List<Long> channelIds) {
        Map<Long, ChannelInfoDto> cachedInfos = channelIds.stream()
                .map(channelId -> cache.get(channelId, ChannelInfoDto.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ChannelInfoDto::channelId, Function.identity()));

        List<Long> notCachedIds = channelIds.stream()
                .filter(id -> !cachedInfos.containsKey(id))
                .toList();

        if (!notCachedIds.isEmpty()) {
            Map<Long, ChannelInfoDto> newInfosMap = super.getChannelInfos(notCachedIds).stream()
                    .collect(Collectors.toMap(ChannelInfoDto::channelId, Function.identity()));
            newInfosMap.forEach(cache::put);

            //기존 map과 병합
            cachedInfos.putAll(newInfosMap);
        }
        return channelIds.stream()
                .map(cachedInfos::get)
                .toList();
    }
}
