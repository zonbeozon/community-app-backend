package com.zonbeozon.channel.service.finder;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.repository.BlogChannelRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogChannelFinder {
    private final BlogChannelRepository blogChannelRepository;

    public BlogChannel findByIdElseThrow(Long id) {
        return blogChannelRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    public boolean existsById(Long id) {
        return blogChannelRepository.existsById(id);
    }

}
