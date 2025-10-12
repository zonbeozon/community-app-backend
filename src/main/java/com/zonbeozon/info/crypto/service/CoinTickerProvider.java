package com.zonbeozon.info.crypto.service;

import com.zonbeozon.info.crypto.dto.CoinTickerDto;

import java.util.Collection;
import java.util.List;

public interface CoinTickerProvider {
    List<CoinTickerDto> provide(Collection<String> symbols);
}
