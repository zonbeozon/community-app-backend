package com.zonbeozon.channel.security;

public enum ChannelAction {
    POST_CREATE, POST_DELETE, POST_UPDATE,

    MODIFY_ROLE,

    CHANNEL_UPDATE, CHANNEL_DELETE,

    READ_KICKED_MEMBER,
    COMMENT_CREATE, COMMENT_DELETE,
    KICK;
}
