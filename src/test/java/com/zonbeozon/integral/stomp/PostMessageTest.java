package com.zonbeozon.integral.stomp;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.*;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.service.PostStompSender;
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
    private PostStompSender postStompSender;

    private BlogChannel channel;
    private Member member;
    private Post post;

    @MockitoSpyBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp() {
        channel = testBlogChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testBlogChannelService.joinAsMember(channel, member);
        post = testPostService.createAndSave(channel, member);
        //postStompSender가 requires_new를 사용하기 떄문에 정보조회를 위해서는 커밋 되어야함
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @AfterEach
    void tearDown() {
        testPostService.clearAll();
        testBlogChannelService.clearAll();
        testMemberService.deleteMember(member);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @DisplayName("post생성시 post생성 메시지가 브로드케스트되어야 한다.")
    @Test
    void postCreationBroadcastsPostCreationMessage()  {
        postStompSender.handlePostCreated(new PostCreatedEvent(channel.getId(), post.getId()));
        ArgumentCaptor<PostEventResponse> responseCaptor = ArgumentCaptor.forClass(PostEventResponse.class);

        Mockito.verify(simpMessagingTemplate)
                .convertAndSend(
                        Mockito.eq("/topic/channel/" + channel.getId() + "/post"),
                        responseCaptor.capture()
                );
        PostEventResponse capturedResponse = responseCaptor.getValue();
        Assertions.assertThat(capturedResponse.type()).isEqualTo(PostEventType.CREATED);
    }

    @DisplayName("post삭제시 post삭제 메시지가 브로드케스트되어야 한다.")
    @Test
    void shouldBroadcastDeleteEventWhenPostIsDeleted()  {
        postStompSender.handlePostDeleted(new PostDeletedEvent(channel.getId(), post.getId()));
        ArgumentCaptor<PostEventResponse> responseCaptor = ArgumentCaptor.forClass(PostEventResponse.class);
        Mockito.verify(simpMessagingTemplate)
                .convertAndSend(
                        Mockito.eq("/topic/channel/" + channel.getId() + "/post"),
                        responseCaptor.capture()
                );
        PostEventResponse capturedResponse = responseCaptor.getValue();
        Assertions.assertThat(capturedResponse.type()).isEqualTo(PostEventType.DELETED);
    }

    @DisplayName("post업데이트시 post업데이트 메시지가 브로드케스트되어야 한다.")
    @Test
    void shouldBroadcastUpdateEventWhenPostIsUpdated()  {
        postStompSender.handlePostUpdated(new PostUpdatedEvent(channel.getId(), post.getId()));
        ArgumentCaptor<PostEventResponse> responseCaptor = ArgumentCaptor.forClass(PostEventResponse.class);

        Mockito.verify(simpMessagingTemplate)
                .convertAndSend(
                        Mockito.eq("/topic/channel/" + channel.getId() + "/post"),
                        responseCaptor.capture()
                );
        PostEventResponse capturedResponse = responseCaptor.getValue();
        Assertions.assertThat(capturedResponse.type()).isEqualTo(PostEventType.UPDATED);
    }
}
