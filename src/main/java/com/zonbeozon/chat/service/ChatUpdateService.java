package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatImage;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.service.ImageFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatUpdateService {
    private final ChatFinder chatFinder;
    private final ImageFinder imageFinder;

    public void updateContent(Long chatId, String content) {
        chatFinder.findByIdElseThrow(chatId).setContent(content);
    }

    public void addImages(Long chatId, List<Long> imageIds) {
        Chat chat = chatFinder.findByIdElseThrow(chatId);
        if(!chat.canAddImage(imageIds.size())) throw new BadRequestException(ErrorCode.MAX_CHAT_IMAGE_REACHED);
        List<ChatImage> chatImages = createChatImage(chat, imageIds);
        chat.addImages(chatImages);
    }

    private List<ChatImage> createChatImage(Chat chat, List<Long> imageIds) {
        return imageIds.stream().map(imageId -> new ChatImage(chat, imageFinder.findByIdElseThrow(imageId))).toList();
    }
}
