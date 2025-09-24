package com.zonbeozon.integral.stomp;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@RestController
public class TestStompMessageReceiver {
    private CompletableFuture<Authentication> authFuture = new CompletableFuture<>();

    @MessageMapping("/connect-success")
    public void receiveConnectSuccess(Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Principal principal = accessor.getUser();

        if (principal instanceof Authentication) {
            authFuture.complete((Authentication) principal);
        } else {
            authFuture.completeExceptionally(new RuntimeException("Principal is not an Authentication object"));
        }
    }

    public CompletableFuture<Authentication> getAuthFuture() {
        return authFuture;
    }

    public void reset() {
        authFuture = new CompletableFuture<>();
    }
}

