package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public void register(ExchangeRequest exchangeRequest) {
        if(isDuplicate(exchangeRequest.englishName())) {
            throw new ExchangeException(ErrorCode.DUPLICATE_EXCHANGE);
        }
        exchangeRepository.save(Exchange.fromDto(exchangeRequest));
    }

    @Transactional(readOnly = true)
    public boolean isDuplicate(String exchangeName) {
        return exchangeRepository.existsByEnglishName(exchangeName);
    }

    @Transactional(readOnly = true)
    public Exchange findByName(String exchangeName) {
        return exchangeRepository.findByEnglishName(exchangeName)
                .orElseThrow(() -> new ExchangeException(ErrorCode.EXCHANGE_NOT_FOUND));
    }
}
