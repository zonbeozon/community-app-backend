package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatCursor;
import com.zonbeozon.chat.domain.ChatImage;
import com.zonbeozon.chat.dto.ChatPayload;
import com.zonbeozon.chat.dto.PagedChatPayload;
import com.zonbeozon.chat.repository.ChatRepository;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryService {
    private final ChatRepository chatRepository;

    public PagedChatPayload getPagedChatPayload(Long chattingGroupId, ChatCursor cursor, int pageSize) {
        CursorPage<Chat, ChatCursor> chats = chatRepository.findByChattingGroupAndCursor(chattingGroupId, cursor, pageSize);
        List<ChatPayload> chatPayloads = convertChatsToChatPayloads(chats.getContent());
        return new PagedChatPayload(chattingGroupId, chatPayloads, chats.getSize(), chats.getTotalElements(), chats.getNextCursor());
    }

    public ChatPayload getReplyExecludedChatPayload(Long chatId) {
        Chat chat = chatRepository.findByIdWithChatImagesAndAuthor(chatId).orElseThrow(() -> new NotFoundException(ErrorCode.CHAT_NOT_FOUND));
        return new ChatPayload(
                chatId,
                chat.getContent(),
                convertChatImagesToDto(chat.getChatImages()),
                MemberDto.from(chat.getAuthor()),
                null,
                chat.getCreatedAt(),
                chat.getModifiedAt()
        );
    }

    private List<ChatPayload> convertChatsToChatPayloads(List<Chat> chats) {
        return chats.stream()
                .map(chat -> new ChatPayload(
                        chat.getId(),
                        chat.getContent(),
                        convertChatImagesToDto(chat.getChatImages()),
                        MemberDto.from(chat.getAuthor()),
                        convertChatsToChatPayloads(chat.getChildren()),
                        chat.getCreatedAt(),
                        chat.getModifiedAt()
                )).toList();
    }

    private List<ImageDto> convertChatImagesToDto(List<ChatImage> chatImages) {
        return chatImages.stream().map(chatImage -> new ImageDto(chatImage.getImage().getId(), chatImage.getImage().getUrl())).toList();
    }
}
