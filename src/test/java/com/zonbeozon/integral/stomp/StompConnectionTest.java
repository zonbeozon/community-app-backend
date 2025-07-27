package com.zonbeozon.integral.stomp;

import com.jayway.jsonpath.JsonPath;
import com.zonbeozon.auth.TestAuthenticationBuilder;
import com.zonbeozon.auth.service.TokenService;
import com.zonbeozon.global.utils.ExceptionUtils;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.websocket.DeploymentException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestStompMessageReceiver.class)
@Transactional
public class StompConnectionTest {
    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TestStompMessageReceiver receiver;

    @MockitoSpyBean
    private TokenService tokenService;
    private Member member;
    private String connectionUrl;
    private String accessToken;

    @BeforeEach
    void setUp() {
        member = new TestMemberBuilder("choi", "choi@gmail.com").persist(entityManager);
        accessToken = tokenService.generateAccessToken(new TestAuthenticationBuilder(member).build());

        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        connectionUrl = "ws://localhost:" + port + "/ws";
    }

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

    @AfterEach
    void tearDown() {
        receiver.reset();
        stompClient.stop();
    }
}
