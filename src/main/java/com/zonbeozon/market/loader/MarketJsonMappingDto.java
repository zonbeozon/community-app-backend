package com.zonbeozon.market.loader;

import lombok.Data;

import java.util.List;

@Data
class MarketJsonMappingDto {
    private String symbol;
    private List<String> markets;
}
