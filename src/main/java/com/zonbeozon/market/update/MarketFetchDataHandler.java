package com.zonbeozon.market.update;

import com.zonbeozon.market.fetch.MarketFetchData;

import java.util.List;

public interface MarketFetchDataHandler {
    void handle(MarketFetchData marketFetchData);
}
