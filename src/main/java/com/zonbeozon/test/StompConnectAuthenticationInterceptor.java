package com.zonbeozon.test;

import com.zonbeozon.auth.AuthenticationTokenUtils;
import com.zonbeozon.global.exception.UnauthenticatedException;
import com.zonbeozon.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompConnectAuthenticationInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            String token = AuthenticationTokenUtils.resolveAuthHeader(authHeader);
            if(token == null)
                throw new ConnectionException(ConnectionException.ErrorCode.UNAUTHORIZED);

            if(!tokenProvider.validateToken(token))
                throw new ConnectionException(ConnectionException.ErrorCode.UNAUTHORIZED);

            Authentication authentication = tokenProvider.getAuthentication(token);
            accessor.setUser(authentication);
        }
        return message;
    }
}
