package com.zonbeozon.market.loader;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.MarketType;
import lombok.Data;

@Data
class MarketRegistry {
    private String symbol;
    private String marketCode;
    private Exchange exchange;
    private MarketType marketType;
    private Currency currency;
}
