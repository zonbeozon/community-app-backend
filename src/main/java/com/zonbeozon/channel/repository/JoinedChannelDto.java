package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JoinedChannelDto {
    private Channel channel;
    private Long memberCount;
    private String latestPostContent;
    private LocalDateTime latestPostCreatedAt;
}
