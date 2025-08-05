package com.zonbeozon.integral.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.zonbeozon.auth.TestAuthenticationBuilder;
import com.zonbeozon.auth.service.TokenService;
import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.respository.MemberRepository;
import com.zonbeozon.post.dto.PostCreateCommand;
import com.zonbeozon.post.dto.PostEventResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.service.PostCreator;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostMessageTest {
    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    private String connectionUrl;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PostCreator postCreator;
    @Autowired
    private ObjectMapper objectMapper;
    private Channel channel;
    private ChannelMember channelMember;
    private Member member;
    private String accessToken;
    private StompSession stompSession;
    private CompletableFuture<String> cfPayload = new CompletableFuture<>();

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        transactionTemplate = new TransactionTemplate(transactionManager);
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        stompClient.setMessageConverter(converter);
        connectionUrl = "ws://localhost:" + port + "/ws";
        joinChannel();
        doConnect();
        stompSession.subscribe("/topic/channel/" + channel.getId() + "/post", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                byte[] payloadBytes = (byte[]) payload;
                String jsonString = new String(payloadBytes, StandardCharsets.UTF_8);
                cfPayload.complete(jsonString);
            }
        });
    }

    @AfterEach
    void tearDown() {
        clearAll();
    }

    @DisplayName("post생성시 post생성 메시지가 브로드케스트되어야 한다.")
    @Test
    void postCreationBroadcastsPostCreationMessage() throws ExecutionException, InterruptedException, TimeoutException {
        Long postId = postCreator.addPost(channel.getId(), new PostCreateCommand("content", List.of()));
        String payload = cfPayload.get(2, TimeUnit.SECONDS);
        Number actual = JsonPath.read(payload, "$.body.postId");
        Assertions.assertThat(actual).isEqualTo((postId.intValue()));
    }

    private void clearAll() {
        transactionTemplate.execute(status -> {
            channelMemberRepository.deleteAllInBatch();
            postRepository.deleteAllInBatch();
            memberRepository.deleteAllInBatch();
            channelRepository.deleteAllInBatch();
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
                new AbstractTestSessionHandler()
        );
        stompSession = cfSession.get(2, TimeUnit.SECONDS);
    }

    private void joinChannel() {
        transactionTemplate.execute(status -> {
            member = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
            accessToken = tokenService.generateAccessToken(new TestAuthenticationBuilder(member).build());
            channel = new TestChannelBuilder().persist(entityManager);
            channelMember = new TestChannelMemberBuilder(member, channel).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
            return null;
        });
    }

}
