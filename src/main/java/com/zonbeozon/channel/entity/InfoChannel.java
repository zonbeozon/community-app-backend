package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfoChannel extends Channel {

    public InfoChannel(
            String title,
            String description,
            String profile,
            ChannelSetting setting,
            ChannelCreatorType creatorType
    ) {
        super(title, description, profile, setting, creatorType);
    }

    //비정규화 필드
    Long latestPostId;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.INFO;
    }
}
