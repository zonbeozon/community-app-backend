package com.zonbeozon.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "채널 목록이 2개일때 응답",
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
            }
          ],
          "totalElements": 2
        }
    """
)
public record JoinedInfoChannelListResponse(
    List<JoinedInfoChannelResponse> channels,
    int totalElements
) {
    public static JoinedInfoChannelListResponse from(List<InfoChannelOverview> channels) {
        return new JoinedInfoChannelListResponse(
                channels.stream().map(JoinedInfoChannelResponse::from).toList(),
                channels.size()
        );
    }
}
