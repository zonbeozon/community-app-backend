package com.zonbeozon.chat;

import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.repository.ChatRepository;
import com.zonbeozon.chat.service.*;
import com.zonbeozon.test.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatIntegrationTest extends AbstractIntegrationTest {
    protected static final String DEFAULT_CHAT_GROUP_NAME = "chat";
    @Autowired
    protected ChatRepository chatRepository;
    @Autowired
    protected ChatCreateService chatCreateService;
    @Autowired
    protected ChatDeleteService chatDeleteService;
    @Autowired
    protected ChatFinder chatFinder;
    @Autowired
    protected ChatUpdateService chatUpdateService;
    @Autowired
    protected ChatQueryService chatQueryService;
    @Autowired
    protected ChattingGroupCreateService chattingGroupCreateService;
    @Autowired
    protected ChattingGroupFinder chattingGroupFinder;

    protected ChattingGroup createChattingGroup() {
         Long id = chattingGroupCreateService.createChattingGroup(DEFAULT_CHAT_GROUP_NAME);
         return chattingGroupFinder.findByIdElseThrow(id);
    }
}
