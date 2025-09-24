package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoDto;

import java.util.List;

public interface ChannelInfoAssembler {
    ChannelInfoDto getChannelInfo(Long channelId);

    /**
     * @throws com.zonbeozon.global.exception.NotFoundException 주어진 `channelIds` 목록 중 존재하지 않는 ID가 하나라도 포함된 경우
     * @return channelIds 입력 순서대로 응답 구성(순서 보장)
     */
    List<ChannelInfoDto> getChannelInfos(List<Long> channelIds);
}
