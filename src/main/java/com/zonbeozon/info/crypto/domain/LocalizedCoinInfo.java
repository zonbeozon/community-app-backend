package com.zonbeozon.info.crypto.domain;

import lombok.Getter;

@Getter
public class LocalizedCoinInfo {
    private String name;
    private String description;

    public LocalizedCoinInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
