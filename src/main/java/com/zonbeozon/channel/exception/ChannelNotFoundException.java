package com.zonbeozon.channel.exception;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException() {
        super("channel not found");
    }
}
