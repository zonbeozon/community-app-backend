package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    @Transactional
    public void addExchange(Exchange exchange) {
        if(isDuplicate(exchange.getEnglishName())) {
            throw new ExchangeException(ErrorCode.DUPLICATE_EXCHANGE);
        }
        exchangeRepository.save(exchange);
    }

    private boolean isDuplicate(String exchangeName) {
        return exchangeRepository.existsByEnglishName(exchangeName);
    }

    @Transactional(readOnly = true)
    public Exchange findByName(String exchangeName) {
        return exchangeRepository.findByEnglishName(exchangeName.toLowerCase())
                .orElseThrow(() -> new ExchangeException(ErrorCode.EXCHANGE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Exchange findById(Long id) {
        return exchangeRepository.findById(id)
                .orElseThrow(() -> new ExchangeException(ErrorCode.EXCHANGE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Exchange> findAll() {
        return exchangeRepository.findAll();
    }

    @PostConstruct
    @Transactional
    public void setup() {
        addExchange(Exchange.builder()
                .englishName("upbit")
                .koreanName("업비트")
                .description("생략")
                .topPriorityMarketType(MarketType.KRW)
                .build());
        addExchange(Exchange.builder()
                .englishName("bithumb")
                .koreanName("빗썸")
                .description("생략")
                .topPriorityMarketType(MarketType.KRW)
                .build());
        addExchange(Exchange.builder()
                .englishName("binance")
                .koreanName("바이낸스")
                .description("생략")
                .topPriorityMarketType(MarketType.USDT)
                .build());
    }
}
