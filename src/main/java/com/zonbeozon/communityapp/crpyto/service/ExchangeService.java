package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public long register(ExchangeRequest exchangeRequest) {
        if(isDuplicate(exchangeRequest.getEnglishName())) {
            throw new RuntimeException("Exchange name already exists");
        }
        return exchangeRepository.save(Exchange.fromDto(exchangeRequest)).getId();
    }

    @Transactional(readOnly = true)
    public boolean isDuplicate(String exchangeName) {
        return exchangeRepository.existsByEnglishName(exchangeName);
    }

    @Transactional(readOnly = true)
    public Exchange findByName(String exchangeName) {
        return exchangeRepository.findByEnglishName(exchangeName)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));
    }
}
