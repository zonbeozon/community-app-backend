package com.zonbeozon.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

public class ListFileLoaderTemplateTest {
    private final static ListFileLoaderTemplate<FooBarBaz> jsonFileLoaderTemplate = new ListFileLoaderTemplate<>(FooBarBaz.class);
    @Test
    @DisplayName("정상 작동 확인")
    void givenValidJsonFile_whenLoad_thenReturnExpectedObjectList() {
        List<FooBarBaz> fooBarBazList = jsonFileLoaderTemplate.load("/testJson.json");
        assertThat(fooBarBazList)
                .hasSize(2)
                .extracting("foo", "bar", "baz")
                .containsExactly(
                        tuple(1, 2, 3),
                        tuple(4, 5, 6)
                );
    }

    @Test
    @DisplayName("file path가 잘못되었을 때 예외 발생")
    void givenInvalidFilePath_whenLoad_thenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> jsonFileLoaderTemplate.load("/illegalPath.json"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static class FooBarBaz {
        private Long foo;
        private BigDecimal bar;
        private Integer baz;
    }
}
