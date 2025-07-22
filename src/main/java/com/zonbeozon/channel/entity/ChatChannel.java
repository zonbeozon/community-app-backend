package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CHAT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatChannel extends Channel {
    public ChatChannel(
            String title,
            String description,
            ChannelSetting setting,
            ChannelCreatorType creatorType
    ) {
        super(title, description, setting, creatorType);
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.CHAT;
    }
}
