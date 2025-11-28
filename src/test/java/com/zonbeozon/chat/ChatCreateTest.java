package com.zonbeozon.chat;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatImage;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ChatCreateTest extends ChatIntegrationTest {
    @Test
    @DisplayName("루트 채팅 생성")
    void createRootChatWithImagesSuccess() {
        Member member = testMemberService.createAndSave();
        List<Image> images = List.of(
                createMockImage("url1", "123", member),
                createMockImage("url2", "456", member)
        );
        ChattingGroup chattingGroup = createChattingGroup();
        Long id = chatCreateService.create(chattingGroup.getId(), member.getId(), "test-chat", null, images.stream().map(Image::getId).toList());
        Chat chat = chatFinder.findByIdElseThrow(id);
        Assertions.assertThat(chat).isNotNull();
        Assertions.assertThat(chat.getChattingGroup()).isEqualTo(chattingGroup);
        Assertions.assertThat(chat.getAuthor()).isEqualTo(member);
        Assertions.assertThat(chat.getChatImages())
                .extracting(ChatImage::getImage)
                .containsExactlyInAnyOrderElementsOf(images);
    }

    @Test
    @DisplayName("자식 채팅 생성")
    void createChildChatWithParentRelationshipSuccess() {
        Member member = testMemberService.createAndSave();

        ChattingGroup chattingGroup = createChattingGroup();
        Chat rootChatting = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member.getId(), "root-chat", null, List.of()));
        Chat childChatting_1 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member.getId(), "child-chat-1", rootChatting.getId(), List.of()));
        Chat childChatting_2 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member.getId(), "child-chat-2", rootChatting.getId(), List.of()));

        Assertions.assertThat(childChatting_1.getParent()).isEqualTo(rootChatting);
        Assertions.assertThat(childChatting_2.getParent()).isEqualTo(rootChatting);
    }

    @Test
    @DisplayName("부모가 루트가 아니면 예외가 발생한다.")
    void createGrandChildChatThrowsExceptionLimitDepth() {
        Member member = testMemberService.createAndSave();
        ChattingGroup chattingGroup = createChattingGroup();

        Chat rootChatting = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member.getId(), "root-chat", null, List.of()));
        Chat childChatting_1 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member.getId(), "child-chat-1", rootChatting.getId(), List.of()));
        Assertions.assertThatThrownBy(() -> chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member.getId(), "child-chat-2", childChatting_1.getId(), List.of())))
                .isInstanceOf(BadRequestException.class);
    }
}
