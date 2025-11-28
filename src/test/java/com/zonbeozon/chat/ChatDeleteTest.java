package com.zonbeozon.chat;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatImage;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChatDeleteTest extends ChatIntegrationTest {
    private ChattingGroup chattingGroup;
    private Member member;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        chattingGroup = createChattingGroup();
        member = testMemberService.createAndSave();
    }
    @Test
    @DisplayName("루트 채팅 삭제시 자식 채팅들도 삭제되어야 한다.")
    void deleteChatCascadeRemovesChildChats() {
        Chat rootChat = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(),member.getId(),"root-chatting", null, null));
        Chat childChat_1 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(),member.getId(),"child-chat-1", rootChat.getId(), null));
        Chat childChat_2 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(),member.getId(),"child-chat-2", rootChat.getId(), null));

        entityManager.flush();
        entityManager.clear();
        chatDeleteService.deleteChat(rootChat.getId());

        Assertions.assertThat(chatRepository.findById(childChat_1.getId())).isEmpty();
        Assertions.assertThat(chatRepository.findById(childChat_2.getId())).isEmpty();
    }

    @Test
    @DisplayName("채팅 삭제시 자식 이미지도 삭제되어야 한다.")
    void deleteChatCascadeRemovesImages() {
        List<Image> images = List.of(
                createMockImage("url1", "123", member),
                createMockImage("url2", "456", member)
        );

        Chat rootChat = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(),member.getId(),"root-chatting", null, null));
        chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(),member.getId(),"child-chat", rootChat.getId(), images.stream().map(Image::getId).toList()));

        entityManager.flush();
        entityManager.clear();

        chatDeleteService.deleteChat(rootChat.getId());

        images.forEach(image -> Assertions.assertThat(imageRepository.findById(image.getId())).isEmpty());
    }

    @Test
    @DisplayName("부분 이미지 삭제시 채팅에서 이미지가 빠진다.")
    void partialDeleteImagesUpdatesChatImageList() {
        List<Image> images = List.of(
                createMockImage("url1", "123", member),
                createMockImage("url2", "456", member)
        );
        Chat chat = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(),member.getId(),"chatting", null, images.stream().map(Image::getId).toList()));
        entityManager.flush();
        entityManager.clear();

        chatDeleteService.deleteImages(chat.getId(), List.of(images.getFirst().getId()));
        Assertions.assertThat(chatFinder.findByIdElseThrow(chat.getId()).getChatImages()).hasSize(1)
                .extracting(ChatImage::getImage)
                .extracting(Image::getId)
                .first()
                .isEqualTo(images.get(1).getId());
    }
}
