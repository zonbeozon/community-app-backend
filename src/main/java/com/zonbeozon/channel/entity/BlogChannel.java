package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("BLOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BlogChannel extends Channel {

    public BlogChannel(
            String title,
            String description,
            ChannelSetting setting
    ) {
        super(title, description, setting, ChannelType.BLOG);
    }

}
