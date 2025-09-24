package com.zonbeozon.base;

import com.zonbeozon.auth.TestAuthenticationBuilder;
import com.zonbeozon.auth.service.TokenService;
import com.zonbeozon.integral.stomp.AbstractTestSessionHandler;
import com.zonbeozon.integral.stomp.ReceiptAckConfig;
import com.zonbeozon.integral.stomp.TestStompMessageReceiver;
import com.zonbeozon.member.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestStompMessageReceiver.class, ReceiptAckConfig.class})
public class AbstractStompTest extends AbstractIntegrationTest {
    @LocalServerPort
    protected int port;
    @Autowired
    protected TestStompMessageReceiver receiver;
    @Autowired
    private TaskScheduler taskScheduler;
    @MockitoSpyBean
    protected TokenService tokenService;

    protected String connectionUrl;
    protected WebSocketStompClient stompClient;
    protected StompSession stompSession;

    protected Member member;
    protected String accessToken;

    @BeforeEach
    void setUp() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(taskScheduler);
        connectionUrl = "ws://localhost:" + port + "/ws";
        member = testMemberService.createAndSave();
        accessToken = tokenService.generateAccessToken(new TestAuthenticationBuilder(member).build());
    }

    @AfterEach
    void tearDown() {
        receiver.reset();
        stompClient.stop();
    }
}
