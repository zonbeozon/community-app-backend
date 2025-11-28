package com.zonbeozon.chat.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatCursor;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.CursorPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zonbeozon.chat.domain.QChat.chat;
import static com.zonbeozon.chat.domain.QChatImage.chatImage;
import static com.zonbeozon.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Chat> findByIdWithChatImages(Long id) {
        JPAQuery<Chat> query = queryFactory.selectFrom(chat)
                .leftJoin(chat.chatImages, chatImage).fetchJoin()
                .leftJoin(chatImage.image).fetchJoin()
                .where(chat.id.eq(id))
                .distinct();

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Optional<Chat> findByIdWithChatImagesAndAuthor(Long id) {
        JPAQuery<Chat> query = queryFactory.selectFrom(chat)
                .leftJoin(chat.chatImages, chatImage).fetchJoin()
                .leftJoin(chatImage.image).fetchJoin()
                .join(chat.author, member).fetchJoin()
                .where(chat.id.eq(id))
                .distinct();

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public CursorPage<Chat, ChatCursor> findByChattingGroupAndCursor(Long chattingGroupId, ChatCursor cursor, int pageSize) {
        List<Chat> result = queryFactory
                .selectFrom(chat)
                .join(chat.author, member).fetchJoin()
                .where(
                        chat.chattingGroup.id.eq(chattingGroupId),
                        cursorCondition(cursor)
                )
                .orderBy(chat.createdAt.desc(), chat.id.desc())
                .limit(pageSize + 1)
                .fetch();

        ChatCursor nextCursor = null;
        boolean hasNext = false;

        if (result.size() > pageSize) {
            hasNext = true;
            result.remove(pageSize);
            Chat lastChat = result.get(result.size() - 1);
            nextCursor = new ChatCursor(lastChat.getCreatedAt(), lastChat.getId());
        }
        Long totalElements = queryFactory
                .select(chat.count())
                .from(chat)
                .where(chat.chattingGroup.id.eq(chattingGroupId))
                .fetchOne();

        return new CursorPageImpl<>(result, nextCursor, totalElements, false ,!hasNext);
    }

    private BooleanExpression cursorCondition(ChatCursor cursor) {
        if (cursor == null) {
            return null;
        }
        return chat.createdAt.lt(cursor.createdAt())
                .or(chat.createdAt.eq(cursor.createdAt()).and(chat.id.lt(cursor.chatId())));
    }
}
