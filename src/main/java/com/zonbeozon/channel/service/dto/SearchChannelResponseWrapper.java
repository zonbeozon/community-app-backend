package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.repository.ChannelWithMemberCount;
import org.springframework.data.domain.Page;

import java.util.List;

public record SearchChannelResponseWrapper (
    List<SearchChannelResponse> content,
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean isFirst,
    boolean isLast,
    boolean hasNext,
    boolean hasPrevious
){
    public static SearchChannelResponseWrapper from(Page<ChannelWithMemberCount> pageData) {
        return new SearchChannelResponseWrapper(
                pageData.getContent().stream().map(SearchChannelResponse::from).toList(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.isFirst(),
                pageData.isLast(),
                pageData.hasNext(),
                pageData.hasPrevious()
        );
    }
}
