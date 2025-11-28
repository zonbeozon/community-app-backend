package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatImage;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.repository.ChatImageRepository;
import com.zonbeozon.chat.repository.ChatRepository;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatCreateService {
    private final ChatRepository chatRepository;
    private final ImageFinder imageFinder;
    private final ChatFinder chatFinder;
    private final MemberFinder memberFinder;
    private final ChattingGroupFinder chattingGroupFinder;

    public Long create(Long chattingGroupId, Long memberId, String content, Long parentId, List<Long> imageIds) {
        Member author = memberFinder.findByIdElseThrow(memberId);
        ChattingGroup chattingGroup = chattingGroupFinder.findByIdElseThrow(chattingGroupId);
        Chat parent = null;
        if(parentId != null) {
            parent = chatFinder.findByIdElseThrow(parentId);
            if(!parent.isRoot()) throw new BadRequestException(ErrorCode.CHAT_REPLY_LIMIT_REACHED);
        }
        Chat chat = new Chat(chattingGroup, author, content, parent);
        chat.setChatImages(createChatImages(chat, imageIds));
        return chatRepository.save(chat).getId();
    }


    private List<ChatImage> createChatImages(Chat chat, List<Long> imageIds) {
        if(imageIds == null || imageIds.isEmpty()) return List.of();
        return imageFinder.findAllByIds(imageIds).stream()
                .map(image -> new ChatImage(chat, image))
                .toList();
    }
}
