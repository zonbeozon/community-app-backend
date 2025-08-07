package com.zonbeozon.global;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompSubscriptionValidateHandler {
    boolean isSupport(String destination);
    void handle(StompHeaderAccessor accessor);
}
