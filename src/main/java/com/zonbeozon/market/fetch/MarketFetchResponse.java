package com.zonbeozon.market.fetch;

import com.zonbeozon.fiat.entity.FiatType;

interface MarketFetchResponse {
    MarketFetchData getResult(FiatType fiatType);
}
