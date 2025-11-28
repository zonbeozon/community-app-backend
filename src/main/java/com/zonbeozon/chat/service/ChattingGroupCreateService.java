package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.repository.ChattingGroupRepository;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChattingGroupCreateService {
    private final ChattingGroupRepository chattingGroupRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initChattingGroups() {
        List<String> groupNames = readGroupNamesFromFile();
        List<ChattingGroup> groupsToCreate = groupNames.stream()
                .filter(name -> !chattingGroupRepository.existsByName(name))
                .map(ChattingGroup::new)
                .toList();
        if(groupsToCreate.isEmpty()) return;
        log.info("새로운 그룹(들)이 생성 되었습니다, {}", groupsToCreate);
        chattingGroupRepository.saveAll(groupsToCreate);
    }

    public Long createChattingGroup(String name) {
        if(chattingGroupRepository.existsByName(name)) {
            throw new ConflictException(ErrorCode.CHATTING_GROUP_NAME_DUPLICATE);
        }
        ChattingGroup chattingGroup = new ChattingGroup(name);
        return chattingGroupRepository.save(chattingGroup).getId();
    }

    private List<String> readGroupNamesFromFile() {
        ClassPathResource resource = new ClassPathResource("data/groups.txt");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                    return reader.lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
        } catch (IOException e) {
            throw new RuntimeException("그룹 읽어오기 실패", e);
        }
    }
}