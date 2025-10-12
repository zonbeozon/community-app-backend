package com.zonbeozon.info.crypto.service;

import com.zonbeozon.global.ListFileLoaderTemplate;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class SupportedCoinListProvider {
    private Set<String> symbols;

    public Set<String> getSupportedCoinSymbols() {
        return symbols;
    }

    @PostConstruct
    public void initializeFromFile() {
        ListFileLoaderTemplate<String> listFileLoaderTemplate = new ListFileLoaderTemplate<>(String.class);
        Set<String> symbolsReadFromFile = Set.copyOf(listFileLoaderTemplate.load("/data/supported-coin-symbols.json"));
        if(symbols.isEmpty()) log.warn("등록된 symbol이 0개 입니다.");
        symbols = symbolsReadFromFile;
    }
}
