package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.channel.entity.CommunityInfoChannel;
import com.zonbeozon.channel.entity.OficialInfoChannel;
import com.zonbeozon.channel.service.ChannelCreateStrategy;
import com.zonbeozon.channel.service.ChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ChannelConfig {

    @Bean
    public ChannelFactory channelFactory() {
        return new ChannelFactory(createChannelCreateStrategies());
    }

    private Map<ChannelType, ChannelCreateStrategy> createChannelCreateStrategies() {
        return Map.of(
                ChannelType.COMMUNITY_INFO, CommunityInfoChannel::create,
                ChannelType.OFFICIAL_INFO, OficialInfoChannel::create
        );
    }
}
