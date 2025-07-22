package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("BLOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogChannel extends Channel {

    public BlogChannel(
            String title,
            String description,
            ChannelSetting setting,
            ChannelCreatorType creatorType
    ) {
        super(title, description, setting, creatorType);
    }

    //비정규화 필드
    Long latestPostId;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.BLOG;
    }

    public void setLatestPostId(Long latestPostId) {
        this.latestPostId = latestPostId;
    }
}
