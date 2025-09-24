package com.zonbeozon.integral.stomp;

import com.jayway.jsonpath.JsonPath;
import com.zonbeozon.base.AbstractStompTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketHttpHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class StompConnectionTest extends AbstractStompTest {

    @DisplayName("jwt 토큰이 없다면 Stomp Connect 요청에서 실패한다.")
    @Test
    void ThrowExceptionIfAuthTokenIsAbsent() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> cfPayload = new CompletableFuture<>();

        stompClient.connectAsync(connectionUrl, new AbstractTestSessionHandler() {
            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                cfPayload.complete(new String(payload));
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
        });

         String payload = cfPayload.get(2, TimeUnit.SECONDS);
         String code = JsonPath.read(payload, "$.code");
         assertThat(code).isEqualTo("UNAUTHENTICATED");

    }


    @DisplayName("인증이 완료되었다면 accessor에 인증정보가 저장되어야 한다.")
    @Test
    void authenticationShouldBeSetInSessionAfterSuccessfulConnection() throws ExecutionException, InterruptedException, TimeoutException {
        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        stompClient.connectAsync(
                connectionUrl,
                new WebSocketHttpHeaders(),
                headers,
                new AbstractTestSessionHandler() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                       session.send("/app/connect-success", "connected");
                    }
                }
        );

        CompletableFuture<Authentication> cfAuth = receiver.getAuthFuture();
        Authentication authentication = cfAuth.get(2, TimeUnit.SECONDS);
        Assertions.assertThat(authentication.getName()).isEqualTo(member.getId().toString());
    }
}
