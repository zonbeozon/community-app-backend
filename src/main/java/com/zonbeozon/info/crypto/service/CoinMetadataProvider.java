package com.zonbeozon.info.crypto.service;

import com.zonbeozon.info.crypto.dto.CoinMetadataDto;

import java.util.Collection;
import java.util.List;


public interface CoinMetadataProvider {
    List<CoinMetadataDto> provide(Collection<String> symbols);
}
