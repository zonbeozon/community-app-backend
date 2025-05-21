package com.zonbeozon.currency.loader;

import lombok.Data;

@Data
class CurrencyRegistry {
    private String enName;
    private String krName;
    private String enDescription;
    private String krDescription;
    private String logo;
    private String symbol;
    private String website;
}
