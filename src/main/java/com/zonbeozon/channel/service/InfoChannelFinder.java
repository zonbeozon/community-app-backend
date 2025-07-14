package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.InfoChannel;
import com.zonbeozon.channel.repository.InfoChannelRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoChannelFinder {
    private final InfoChannelRepository infoChannelRepository;

    public InfoChannel findById(Long id) {
        return infoChannelRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
