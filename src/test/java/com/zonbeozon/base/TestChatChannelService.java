package com.zonbeozon.base;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.entity.ChatChannel;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.repository.ChatChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestChatChannelService extends TestChannelService {
    @Autowired
    private ChatChannelRepository channelRepository;

    public ChatChannel createAndSave(
            String channelName,
            ChannelSetting channelSetting
    ) {
        ChatChannel chatChannel = new ChatChannel(channelName, channelName + "'s description", channelSetting);
        return channelRepository.save(chatChannel);
    }

    public ChatChannel createAndSave() {
        return createAndSave(DEFAULT_NAME, DEFAULT_CHANNEL_SETTING);
    }

    public ChatChannel createAndSave(String channelName) {
        return createAndSave(channelName, DEFAULT_CHANNEL_SETTING);
    }
}
