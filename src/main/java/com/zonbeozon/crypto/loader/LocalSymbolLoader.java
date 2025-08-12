package com.zonbeozon.crypto.loader;

import com.zonbeozon.global.ListFileLoaderTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LocalSymbolLoader implements SymbolLoader {
    private static final String FILE_PATH = "/data/symbols.json";
    private final ListFileLoaderTemplate<String> loaderTemplate = new ListFileLoaderTemplate<>(String.class);

    public List<String> load() {
        return loaderTemplate.load(FILE_PATH);
    }

}
