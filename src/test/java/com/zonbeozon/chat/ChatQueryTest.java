package com.zonbeozon.chat;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatCursor;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.dto.ChatPayload;
import com.zonbeozon.chat.dto.PagedChatPayload;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatQueryTest extends ChatIntegrationTest{
    @Autowired
    private EntityManager em;
    private ChattingGroup chattingGroup;
    private Member member_1;
    private Member member_2;
    private Chat rootChatA, childChatA_1, childChatA_2, childChatA_3;
    private Chat rootChatB, childChatB_1, childChatB_2;
    private Chat rootChatC, childChatC_1, childChatC_2, childChatC_3;

    @BeforeEach
    void setup() {
        chattingGroup = createChattingGroup();
        member_1 = testMemberService.createAndSave("member_1");
        member_2 = testMemberService.createAndSave("member_2");
        rootChatA = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_1.getId(), "rootChatA", null, null));
        childChatA_1 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_1.getId(), "childChatA_1", rootChatA.getId(), null));
        childChatA_2 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_2.getId(), "childChatA_2", rootChatA.getId(), null));
        childChatA_3 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_2.getId(), "childChatA_3", rootChatA.getId(), null));
        rootChatB = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_1.getId(), "rootChatB", null, null));
        childChatB_1 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_2.getId(), "childChatB_1", rootChatB.getId(), null));
        childChatB_2 = chatFinder.findByIdElseThrow(chatCreateService.create(chattingGroup.getId(), member_2.getId(), "childChatB_2", rootChatB.getId(), null));
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("페이징 조회시 null커서가 온다면 가장 최근 생성 Chat부터 조회한다.")
    void fetchFirstPageSuccess() {
        PagedChatPayload payload = chatQueryService.getPagedChatPayload(chattingGroup.getId(), null, 1);
        Assertions.assertThat(payload).isNotNull();
        Assertions.assertThat(payload.content()).hasSize(1);
        Assertions.assertThat(payload.content().getFirst().chatId()).isEqualTo(rootChatB.getId());
        Assertions.assertThat(payload.content().getFirst().replies()).hasSize(2)
                .extracting(ChatPayload::chatId)
                .containsExactly(childChatB_2.getId(), childChatB_1.getId());
    }

    @Test
    @DisplayName("페이징 조회시 다음 커서가 반환되어야 한다.")
    void verifyNextCursorWhenHasNext() {
        PagedChatPayload payload = chatQueryService.getPagedChatPayload(chattingGroup.getId(), null, 1);
        Assertions.assertThat(payload.nextCursor()).isEqualTo(new ChatCursor(rootChatB.getCreatedAt(), rootChatB.getId()));
    }

    @Test
    @DisplayName("마지막 조회라면 커서가 null로 반환된다.")
    void verifyLastPageNextCursorIsNull() {
        PagedChatPayload payload = chatQueryService.getPagedChatPayload(chattingGroup.getId(), new ChatCursor(rootChatB.getCreatedAt(), rootChatB.getId()), 2);
        Assertions.assertThat(payload.nextCursor()).isNull();
        Assertions.assertThat(payload.size()).isEqualTo(1);
        Assertions.assertThat(payload.isLast()).isTrue();
        Assertions.assertThat(payload.content().getFirst().chatId()).isEqualTo(rootChatA.getId());
    }
}
