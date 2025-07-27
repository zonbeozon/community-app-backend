package com.zonbeozon.integral.stomp;

import com.jayway.jsonpath.JsonPath;
import com.zonbeozon.auth.TestAuthenticationBuilder;
import com.zonbeozon.auth.service.TokenService;
import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.respository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AckResponseConfig.class)
public class ChannelSubscribeTest {
    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    private String connectionUrl;
    @MockitoSpyBean
    private TokenService tokenService;
    private StompSession stompSession;

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChannelFinder channelFinder;

    @Autowired
    private TaskScheduler taskScheduler;

    private Member member;
    private String accessToken;
    private Channel channel;
    private ChannelMember channelMember;

    private CompletableFuture<String> cfError = new CompletableFuture<>();

    @BeforeEach
    void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(taskScheduler);
        connectionUrl = "ws://localhost:" + port + "/ws";
        clearAll();
    }


    @DisplayName("채널에 참가된 유저면 구독된다.")
    @Test
    void subscribeWhenUserIsChannelMember() throws ExecutionException, InterruptedException, TimeoutException {
        transactionTemplate.execute(status -> {
            member = new TestMemberBuilder("choi", "choi@gmail.com").persist(entityManager);
            accessToken = tokenService.generateAccessToken(new TestAuthenticationBuilder(member).build());
            channel = new TestChannelBuilder().persist(entityManager);
            channelMember = new TestChannelMemberBuilder(member, channel).persist(entityManager);
            return null;
        });

        CompletableFuture<Object> cfReceipt = new CompletableFuture<>();

        doConnect();
        stompSession.subscribe("/topic/channel/" + channel.getId(), new AbstractTestStompFrameHandler())
                .addReceiptTask(stompHeaders -> {
                    cfReceipt.complete(null);
                });

        cfReceipt.get(2, TimeUnit.SECONDS);
    }

    @DisplayName("채널에 참가하지 않은 유저라면 에러 메시지를 보낸다.")
    @Test
    void sendErrorMessageWhenUserIsNotChannelMember() throws ExecutionException, InterruptedException, TimeoutException {
        transactionTemplate.execute(status -> {
            member = new TestMemberBuilder("choi", "choi@gmail.com").persist(entityManager);
            accessToken = tokenService.generateAccessToken(new TestAuthenticationBuilder(member).build());
            channel = new TestChannelBuilder().persist(entityManager);
            return null;
        });

        doConnect();
        stompSession.subscribe("/topic/channel/" + channel.getId(), new AbstractTestStompFrameHandler());

        String payload = cfError.get(5, TimeUnit.SECONDS);
        String code = JsonPath.read(payload, "$.code");
        assertThat(code).isEqualTo("FORBIDDEN");
    }

    private void clearAll() {
        transactionTemplate.execute(status -> {
            memberRepository.deleteAllInBatch();
            channelRepository.deleteAllInBatch();
            channelMemberRepository.deleteAllInBatch();
            return null;
        });
    }

    private void doConnect() throws ExecutionException, InterruptedException, TimeoutException {
        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        CompletableFuture<StompSession> cfSession = stompClient.connectAsync(
                connectionUrl,
                new WebSocketHttpHeaders(),
                headers,
                new AbstractTestSessionHandler() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        session.setAutoReceipt(true);
                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                        cfError.complete(new String(payload));
                    }

                }
        );

        stompSession = cfSession.get(2, TimeUnit.SECONDS);
    }
}
