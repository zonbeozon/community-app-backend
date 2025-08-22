package com.zonbeozon.base;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.repository.BlogChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestBlogChannelService extends TestChannelService {
    @Autowired
    private BlogChannelRepository channelRepository;

    public BlogChannel createAndSave(
            String channelName,
            ChannelSetting channelSetting,
            ChannelCreatorType creatorType
    ) {
        BlogChannel blogChannel = new BlogChannel(channelName, channelName + "'s description", channelSetting, creatorType);
        return channelRepository.save(blogChannel);
    }

    public BlogChannel createAndSave() {
        return createAndSave(DEFAULT_NAME, DEFAULT_CHANNEL_SETTING, DEFAULT_CREATOR_TYPE);
    }

    public BlogChannel createAndSave(String channelName) {
        return createAndSave(channelName, DEFAULT_CHANNEL_SETTING, DEFAULT_CREATOR_TYPE);
    }

}
