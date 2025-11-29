package com.zonbeozon.integral.stomp;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.*;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.service.PostMessageSendService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.transaction.TestTransaction;

public class PostMessageTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostMessageSendService postStompSender;

    private Channel channel;
    private Member member;
    private Post post;

    @MockitoSpyBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp() {
        channel = testChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testChannelService.joinAsMember(channel, member);
        post = testPostService.createAndSave(channel, member);
        //postStompSender가 requires_new를 사용하기 떄문에 정보조회를 위해서는 커밋 되어야함
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @AfterEach
    void tearDown() {
        testPostService.clearAll();
        testChannelService.clearAll();
        testMemberService.deleteMember(member);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @DisplayName("post생성시 post생성 메시지가 브로드케스트되어야 한다.")
    @Test
    void postCreationBroadcastsPostCreationMessage()  {
        postStompSender.handlePostCreated(new PostEvent.Created(channel.getId(), post.getId()));
        ArgumentCaptor<PostEventMessage> responseCaptor = ArgumentCaptor.forClass(PostEventMessage.class);

        Mockito.verify(simpMessagingTemplate)
                .convertAndSend(
                        Mockito.eq("/topic/channel/" + channel.getId() + "/post"),
                        responseCaptor.capture()
                );
        PostEventMessage capturedResponse = responseCaptor.getValue();
        Assertions.assertThat(capturedResponse.type()).isEqualTo(PostEventType.CREATED);
    }

    @DisplayName("post삭제시 post삭제 메시지가 브로드케스트되어야 한다.")
    @Test
    void shouldBroadcastDeleteEventWhenPostIsDeleted()  {
        postStompSender.handlePostDeleted(new PostEvent.Deleted(channel.getId(), post.getId()));
        ArgumentCaptor<PostEventMessage> responseCaptor = ArgumentCaptor.forClass(PostEventMessage.class);
        Mockito.verify(simpMessagingTemplate)
                .convertAndSend(
                        Mockito.eq("/topic/channel/" + channel.getId() + "/post"),
                        responseCaptor.capture()
                );
        PostEventMessage capturedResponse = responseCaptor.getValue();
        Assertions.assertThat(capturedResponse.type()).isEqualTo(PostEventType.DELETED);
    }

    @DisplayName("post업데이트시 post업데이트 메시지가 브로드케스트되어야 한다.")
    @Test
    void shouldBroadcastUpdateEventWhenPostIsUpdated()  {
        postStompSender.handlePostUpdated(new PostEvent.Updated(channel.getId(), post.getId()));
        ArgumentCaptor<PostEventMessage> responseCaptor = ArgumentCaptor.forClass(PostEventMessage.class);

        Mockito.verify(simpMessagingTemplate)
                .convertAndSend(
                        Mockito.eq("/topic/channel/" + channel.getId() + "/post"),
                        responseCaptor.capture()
                );
        PostEventMessage capturedResponse = responseCaptor.getValue();
        Assertions.assertThat(capturedResponse.type()).isEqualTo(PostEventType.UPDATED);
    }
}
