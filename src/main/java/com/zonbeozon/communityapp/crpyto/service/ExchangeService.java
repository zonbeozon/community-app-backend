package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.controller.dto.exchange.ExchangeResponseWrapper;
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

    public ExchangeResponseWrapper createEntireExchangesResponse() {
        List<ExchangeResponseWrapper.ExchangeResponse> exchangeResponses = exchangeRepository.findAll().stream()
                .map(exchange -> ExchangeResponseWrapper.ExchangeResponse.builder()
                        .englishName(exchange.getEnglishName())
                        .koreanName(exchange.getKoreanName())
                        .englishDescription(exchange.getEnglishDescription())
                        .logo(exchange.getLogo())
                        .build())
                .toList();
        return ExchangeResponseWrapper.builder()
                .exchanges(exchangeResponses)
                .size(exchangeResponses.size())
                .build();
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
    public void addDefaultExchanges() {
        addExchange(Exchange.builder()
                .englishName("upbit")
                .koreanName("업비트")
                .englishDescription("Upbit is the largest cryptocurrency exchange in South Korea in terms of trading volume and customer base, with over 180 crypto coins/tokens listed and over 300 trading pairs readily available. Upbit's ecosystem comprises of Upbit Exchange, NFT Marketplace/Drops platform, and its Staking services.")
                .topPriorityMarketType(MarketType.KRW)
                .logo("https://s2.coinmarketcap.com/static/img/exchanges/64x64/351.png")
                .build());
        addExchange(Exchange.builder()
                .englishName("bithumb")
                .koreanName("빗썸")
                .englishDescription("Bithumb is one of South Korea's largest crypto exchanges, with $205 million in daily trading volume at the time of writing and with 170+ listed cryptocurrencies.")
                .topPriorityMarketType(MarketType.KRW)
                .logo("https://s2.coinmarketcap.com/static/img/exchanges/64x64/200.png")
                .build());
        addExchange(Exchange.builder()
                .englishName("binance")
                .koreanName("바이낸스")
                .englishDescription("Binance is the world’s largest crypto exchange by trading volume, with $76 billion daily trading volume on Binance exchange as of August 2022, and 90 million customers worldwide. The platform has established itself as a trusted member of the crypto space, where users can buy, sell and store their digital assets, as well as access over 350 cryptocurrencies listed and thousands of trading pairs. The Binance ecosystem now comprises of Binance Exchange, Labs, Launchpad, Info, Academy, Research, Trust Wallet, Charity, NFT and more.")
                .topPriorityMarketType(MarketType.USDT)
                .logo("https://s2.coinmarketcap.com/static/img/exchanges/64x64/270.png")
                .build());
    }
}
