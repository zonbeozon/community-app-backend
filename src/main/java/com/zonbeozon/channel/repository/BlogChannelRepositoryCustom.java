package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.BlogChannel;

import java.util.List;

public interface BlogChannelRepositoryCustom {
    List<BlogChannel> findAllByMemberId(Long memberId);
}
