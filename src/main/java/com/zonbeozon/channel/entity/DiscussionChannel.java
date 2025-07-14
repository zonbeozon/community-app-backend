package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("DISCUSSION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscussionChannel extends Channel {
    public DiscussionChannel(
            String title,
            String description,
            String profile,
            ChannelSetting setting,
            ChannelCreatorType creatorType
    ) {
        super(title, description, profile, setting, creatorType);
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.DISCUSSION;
    }
}
