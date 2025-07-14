package com.zonbeozon.global;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ListFileLoaderTemplate<T> {
    private final Class<T> clazz;

    public ListFileLoaderTemplate(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> load(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new IllegalStateException("파일을 찾을 수 없습니다: " + filePath);
            }
            JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(is,javaType);
        } catch (IOException e) {
            throw new IllegalStateException("데이터를 읽는 중 오류 발생", e);
        }
    }
}
