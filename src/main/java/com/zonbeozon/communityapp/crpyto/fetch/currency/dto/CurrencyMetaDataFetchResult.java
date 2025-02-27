package com.zonbeozon.communityapp.crpyto.fetch.currency.dto;

import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyMetaData;

import java.util.List;

public record CurrencyMetaDataFetchResult(
        List<CurrencyMetaData> currencyMetaDataList
) {
}
