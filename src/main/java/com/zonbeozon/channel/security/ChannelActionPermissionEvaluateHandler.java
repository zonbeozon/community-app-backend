package com.zonbeozon.channel.security;

import org.aspectj.lang.JoinPoint;

public interface ChannelActionPermissionEvaluateHandler {
    void handle(JoinPoint joinPoint);
    boolean isSupport(ChannelAction action);
}
