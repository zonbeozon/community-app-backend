package com.zonbeozon.channel.exception;

public class ChannelMemberNotFoundException extends ChannelException {
    public ChannelMemberNotFoundException() {
        super("Channel Member not found");
    }
}
