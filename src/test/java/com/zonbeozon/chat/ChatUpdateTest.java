package com.zonbeozon.chat;

import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatUpdateTest extends ChatIntegrationTest {
    private ChattingGroup chattingGroup;
    private Member member;
    @BeforeEach
    void setup() {
        chattingGroup = createChattingGroup();
        member = testMemberService.createAndSave();
    }

    @Test
    @DisplayName("채팅 내용이 수정되어야 한다.")
    void updateChatContentSuccess() {
        Long id = chatCreateService.create(chattingGroup.getId(), member.getId(), "test-chat", null, null);
        chatUpdateService.updateContent(id, "modify-chat");
        Assertions.assertThat(chatFinder.findByIdElseThrow(id).getContent()).isEqualTo("modify-chat");
    }
}
