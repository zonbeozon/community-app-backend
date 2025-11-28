package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatImage;
import com.zonbeozon.chat.repository.ChatImageRepository;
import com.zonbeozon.chat.repository.ChatRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatDeleteService {
    private final ChatFinder chatFinder;
    private final ChatImageRepository chatImageRepository;
    private final ChatRepository chatRepository;
    private final ImageDeleter imageDeleter;

    public void deleteChat(Long chatId) {
        Chat chat = chatFinder.findByIdWithChatImagesElseThrow(chatId);
        List<ChatImage> chatImages = new ArrayList<>();
        collectAllChatImages(chat, chatImages);
        deleteImages(chatImages);
        chatRepository.deleteById(chat.getId());
    }

    public void deleteImages(Long chatId, List<Long> imageIds) {
        if(imageIds == null || imageIds.isEmpty()) return;
        Chat chat = chatFinder.findByIdWithChatImagesElseThrow(chatId);
        List<ChatImage> found = chat.getChatImages().stream()
                .filter(chatImage -> imageIds.contains(chatImage.getImage().getId()))
                .toList();
        if(found.size() != imageIds.size()) throw new NotFoundException(ErrorCode.CHAT_IMAGE_NOT_FOUND);
        deleteImages(found);
    }

    private void deleteImages(List<ChatImage> chatImages) {
        chatImageRepository.deleteAll(chatImages);
        imageDeleter.deleteImages(chatImages.stream().map(ChatImage::getImage).map(Image::getId).toList());
        chatImages.forEach(chatImage -> chatImage.getChat().getChatImages().remove(chatImage));
    }

    private void collectAllChatImages(Chat chat, List<ChatImage> collector) {
        List<ChatImage> curChatImages = chat.getChatImages();
        collector.addAll(curChatImages);
        chat.getChildren().forEach(child -> collectAllChatImages(child, collector));
    }
}
