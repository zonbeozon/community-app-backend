package com.zonbeozon.chat.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.dto.ChatContentUpdateRequest;
import com.zonbeozon.chat.dto.ChatCreateRequest;
import com.zonbeozon.chat.dto.ChatImagesAddRequest;
import com.zonbeozon.chat.dto.ChatImagesDeleteRequest;
import com.zonbeozon.chat.service.*;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
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
    private final ChattingGroupFinder chattingGroupFinder;

    public Long createChat(Long chattingGroupId, ChatCreateRequest request) {
        Member member = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(member.getId(), request.imageIds());
        return chatCreateService.create(chattingGroupId, member.getId(), request.content(), request.parentId(), request.imageIds());
    }

    public Long createChat(String chattingGroupName , ChatCreateRequest request) {
        ChattingGroup chattingGroup = chattingGroupFinder.findByNameElseThrow(chattingGroupName);
        return createChat(chattingGroup.getId(), request);
    }

    public void addChatImages(Long chatId, ChatImagesAddRequest request) {
        Member member = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(member.getId(), request.imageIds());
        chatAuthorizationService.verifyOwner(chatId, member.getId());
        chatUpdateService.addImages(chatId, request.imageIds());
    }

    public void updateContent(Long chatId, ChatContentUpdateRequest request) {
        Member member = authenticationService.getCurrentMember();
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        chatUpdateService.updateContent(chatId, request.content());
    }

    public void deleteChat(Long chatId) {
        Member member = authenticationService.getCurrentMember();
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        chatDeleteService.deleteChat(chatId);
    }

    public void deleteChatImages(Long chatId, ChatImagesDeleteRequest request) {
        Member member = authenticationService.getCurrentMember();
        chatAuthorizationService.verifyOwner(member.getId(), chatId);
        chatDeleteService.deleteImages(chatId, request.imageIds());
    }
}
