package com.zonbeozon.chat;

import com.zonbeozon.chat.api.ChatCommendApi;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.dto.*;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatEventTest extends ChatIntegrationTest {
    @Autowired
    private ChatCommendApi chatCommendApi;
    @Autowired
    private EntityManager em;

    private ChattingGroup chattingGroup;
    private Member member;

    @BeforeEach
    void setup() {
        chattingGroup = createChattingGroup();
        member = testMemberService.createAndSave();
        testMemberService.setSecurityContext(member);
    }

    @Test
    @DisplayName("생성 이벤트를 발생시킨다.")
    void publishesCreatedEventWithCorrectValue() {
        Long id = chatCommendApi.createChat(chattingGroup.getId(), new ChatCreateRequest("content", List.of(), null));
        List<ChatEvent.Created> events = applicationEvents.stream(ChatEvent.Created.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).chatId).isEqualTo(id);
        assertThat(events.get(0).chattingGroupId).isEqualTo(chattingGroup.getId());
    }

    @Test
    @DisplayName("삭제 이벤트를 발생시킨다.")
    void publishesDeletedEventWithCorrectValue() {
        Long id = chatCommendApi.createChat(chattingGroup.getId(), new ChatCreateRequest("content", List.of(), null));
        chatCommendApi.deleteChat(id);
        List<ChatEvent.Deleted> events = applicationEvents.stream(ChatEvent.Deleted.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).chatId).isEqualTo(id);
        assertThat(events.get(0).chattingGroupId).isEqualTo(chattingGroup.getId());
    }

    @Test
    @DisplayName("content 업데이트시 업데이트 이벤트를 발생시킨다.")
    void publishesUpdatedEventWhenContentUpdateWithCorrectValue() {
        Long id = chatCommendApi.createChat(chattingGroup.getId(), new ChatCreateRequest("content", List.of(), null));
        chatCommendApi.updateContent(id, new ChatContentUpdateRequest("update-content"));
        assertUpdatedEventOccurred(id);
    }

    @Test
    @DisplayName("이미지 추가시 업데이트 이벤트를 발생시킨다.")
    void publishesUpdatedEventWhenImageAddingWithCorrectValue() {
        Image image = createMockImage("mock-url", "mock-key", member);
        Long id = chatCommendApi.createChat(chattingGroup.getId(), new ChatCreateRequest("content", List.of(), null));
        em.flush(); em.clear();
        chatCommendApi.addChatImages(id, new ChatImagesAddRequest(List.of(image.getId())));
        assertUpdatedEventOccurred(id);
    }

    @Test
    @DisplayName("채팅 이미지 일부 삭제시 업데이트 이벤트를 발생시킨다.")
    void publishesUpdatedEventWhenImageDeletingWithCorrectValue() {
        Image image = createMockImage("mock-url", "mock-key", member);
        Long id = chatCommendApi.createChat(chattingGroup.getId(), new ChatCreateRequest("content", List.of(image.getId()), null));
        em.flush(); em.clear();
        chatCommendApi.deleteChatImages(id, new ChatImagesDeleteRequest(List.of(image.getId())));
        assertUpdatedEventOccurred(id);
    }

    private void assertUpdatedEventOccurred(Long expectedChatId) {
        List<ChatEvent.Updated> events = applicationEvents.stream(ChatEvent.Updated.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).chatId).isEqualTo(expectedChatId);
        assertThat(events.get(0).chattingGroupId).isEqualTo(chattingGroup.getId());
    }
}
