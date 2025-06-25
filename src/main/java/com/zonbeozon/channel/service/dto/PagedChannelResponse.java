package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.repository.ChannelWithMemberCount;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedChannelResponse(
    List<ChannelResponse> content,
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean isFirst,
    boolean isLast,
    boolean hasNext,
    boolean hasPrevious
){
    public static PagedChannelResponse from(Page<ChannelWithMemberCount> pageData) {
        return new PagedChannelResponse(
                pageData.getContent().stream().map(ChannelResponse::from).toList(),
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
