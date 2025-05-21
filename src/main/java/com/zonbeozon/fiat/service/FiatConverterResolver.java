package com.zonbeozon.fiat.service;

import com.zonbeozon.fiat.FiatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FiatConverterResolver {
    private final List<FiatConverter> converters;

    public void resolve(Object source) {
        FiatConverter fiatConverter = converters.stream()
                .filter(converter -> converter.isSupported(source))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(source + "를 지원하는 converter가 등록되지 않았습니다."));
        fiatConverter.convert(source);
    }
}
