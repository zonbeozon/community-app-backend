package com.zonbeozon.channel.entity;

public enum ChannelType {
    /**
     * 일반 유저가 생성하는 채널
     * chat 패키지는 Community Info 채널이 사용하는 패키지이다.
     */
    COMMUNITY_INFO,
    /**
     * 서버 운영자가 생성하는 채널
     * discussion 패키지는 Official Info 채널이 사용하는 패키지이다.
     */
    OFFICIAL_INFO;
}
