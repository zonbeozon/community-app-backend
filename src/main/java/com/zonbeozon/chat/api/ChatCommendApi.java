package com.zonbeozon.chat.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.dto.*;
import com.zonbeozon.chat.service.*;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class ChatCommendApi {
    private final AuthenticationService authenticationService;
    private final ChatAuthorizationService chatAuthorizationService;
    private final ChatCreateService chatCreateService;
    private final ChatUpdateService chatUpdateService;
    private final ChatDeleteService chatDeleteService;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final ApplicationEventPublisher eventPublisher;
    private final ChatFinder chatFinder;

    public Long createChat(Long chattingGroupId, ChatCreateRequest request) {
        Member member = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(member.getId(), request.imageIds());
        Long chatId = chatCreateService.create(chattingGroupId, member.getId(), request.content(), request.parentId(), request.imageIds());
        eventPublisher.publishEvent(new ChatEvent.Created(chattingGroupId, chatId));
        return chatId;
    }

    public void addChatImages(Long chatId, ChatImagesAddRequest request) {
        Member member = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(member.getId(), request.imageIds());
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        chatUpdateService.addImages(chatId, request.imageIds());
        Long chattingGroupId = chatFinder.findByIdElseThrow(chatId).getChattingGroup().getId();
        eventPublisher.publishEvent(new ChatEvent.Updated(chattingGroupId ,chatId));
    }

    public void updateContent(Long chatId, ChatContentUpdateRequest request) {
        Member member = authenticationService.getCurrentMember();
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        chatUpdateService.updateContent(chatId, request.content());
        Long chattingGroupId = chatFinder.findByIdElseThrow(chatId).getChattingGroup().getId();
        eventPublisher.publishEvent(new ChatEvent.Updated(chattingGroupId ,chatId));
    }

    public void deleteChat(Long chatId) {
        Member member = authenticationService.getCurrentMember();
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        Long chattingGroupId = chatFinder.findByIdElseThrow(chatId).getChattingGroup().getId();
        chatDeleteService.deleteChat(chatId);
        eventPublisher.publishEvent(new ChatEvent.Deleted(chattingGroupId ,chatId));
    }

    public void deleteChatImages(Long chatId, ChatImagesDeleteRequest request) {
        Member member = authenticationService.getCurrentMember();
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        chatDeleteService.deleteImages(chatId, request.imageIds());
        Long chattingGroupId = chatFinder.findByIdElseThrow(chatId).getChattingGroup().getId();
        eventPublisher.publishEvent(new ChatEvent.Updated(chattingGroupId ,chatId));
    }
}
