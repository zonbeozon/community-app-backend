package com.zonbeozon.crypto.loader;

/**
 * load fields: symbol, names, descriptions, logo, website
 */
public interface MetadataLoader {
    CurrencyRegistryHolder load();
}
