package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.repository.JoinedChannelDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "사용자가 가입한 채널 목록이 2개일때 응답",
        example = """
        {
          "channels": [
            {
              "channelId": 1,
              "title": "코인 정보 공유방",
              "profile": "https://example.com/profile1.png",
              "description": "최신 코인 뉴스와 분석을 공유하는 채널입니다.",
              "channelType": "COMMUNITY_INFO",
              "channelJoinLevel": "OPEN",
              "contentOpenLevel": "PUBLIC",
              "memberCount": 124,
              "latestPostContent": "비트코인 ETF 승인 소식!",
              "latestPostCreatedAt": "2024-06-25T14:30:00"
            },
            {
              "channelId": 2,
              "title": "공식 공지 채널",
              "profile": "https://example.com/profile2.png",
              "description": "운영팀의 공식 공지사항을 전달합니다.",
              "channelType": "OFFICIAL_INFO",
              "channelJoinLevel": "DENY",
              "contentOpenLevel": "PUBLIC",
              "memberCount": 3540,
              "latestPostContent": "서버 점검 안내 - 6/30",
              "latestPostCreatedAt": "2024-06-28T10:00:00"
            }
          ],
          "totalElements": 2
        }
    """
)
public record JoinedChannelListResponse(
    List<JoinedChannelResponse> channels,
    int totalElements
) {
    public static JoinedChannelListResponse from(List<JoinedChannelDto> joinedChannels) {
        return new JoinedChannelListResponse(
                joinedChannels.stream().map(JoinedChannelResponse::from).toList(),
                joinedChannels.size()
        );
    }
}
