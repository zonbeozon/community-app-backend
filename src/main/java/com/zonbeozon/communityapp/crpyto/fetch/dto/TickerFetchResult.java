package com.zonbeozon.communityapp.crpyto.fetch.dto;

import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TickerFetchResult {
    private List<Ticker> tickers;
    private String exchangeName;
}
