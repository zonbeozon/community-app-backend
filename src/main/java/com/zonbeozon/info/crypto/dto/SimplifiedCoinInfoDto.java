package com.zonbeozon.info.crypto.dto;

public record SimplifiedCoinInfoDto(
        String symbol,
        String logo,
        String name,
        Long rank,
        Long chattingGroupId
) {
}
