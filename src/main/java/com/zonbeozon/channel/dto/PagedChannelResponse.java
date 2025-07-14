package com.zonbeozon.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "페이지 처리된 채널 응답", example = """
{
  "content": [
    {
      "id": 1,
      "title": "러그 당한 사람 모임",
      "description": "조롱 금지입니다.",
      "profile": "https://example.com/image.png",
      "type": "COMMUNITY_INFO",
      "contentOpenLevel": "PUBLIC",
      "joinLevel": "OPEN",
      "searchLevel": "PUBLIC",
      "memberCount": 88
    }
  ],
  "page": 0,
  "size": 10,
  "totalPages": 5,
  "totalElements": 50,
  "isFirst": true,
  "isLast": false,
  "hasNext": true,
  "hasPrevious": false
}
""")
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
