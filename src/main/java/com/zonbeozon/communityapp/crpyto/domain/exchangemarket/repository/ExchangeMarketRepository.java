package com.zonbeozon.communityapp.crpyto.domain.exchangemarket.repository;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeMarketRepository extends JpaRepository<ExchangeMarket, Long> {
    List<ExchangeMarket> findByExchange(Exchange exchange);
    @Query("SELECT em FROM ExchangeMarket em JOIN FETCH em.market WHERE em.exchange = :exchange")
    List<ExchangeMarket> findByExchangeJoinMarket(Exchange exchange);

}
