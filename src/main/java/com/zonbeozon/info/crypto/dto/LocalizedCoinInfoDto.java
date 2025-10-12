package com.zonbeozon.info.crypto.dto;

import com.zonbeozon.info.crypto.domain.LocalizedCoinInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocalizedCoinInfoDto {
    private String name;
    private String description;

    public static LocalizedCoinInfoDto from(LocalizedCoinInfo localizedCoinInfo) {
        return new LocalizedCoinInfoDto(
                localizedCoinInfo.getName(),
                localizedCoinInfo.getDescription()
        );
    }
}
