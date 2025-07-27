package com.zonbeozon.auth;

import com.zonbeozon.auth.service.TokenParser;
import com.zonbeozon.auth.service.TokenValidator;
import com.zonbeozon.global.exception.stomp.ConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompConnectAuthenticationInterceptor implements ChannelInterceptor {
    private final TokenParser tokenParser;
    private final TokenValidator tokenValidator;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            String token = AuthenticationTokenUtils.resolveAuthHeader(authHeader);
            if(token == null)
                throw new ConnectionException(ConnectionException.ErrorCode.UNAUTHENTICATED);

            if(!tokenValidator.validateToken(token))
                throw new ConnectionException(ConnectionException.ErrorCode.EXPIRED_TOKEN);

            Authentication authentication = tokenParser.getAuthentication(token);
            accessor.setUser(authentication);
        }
        return message;
    }
}
