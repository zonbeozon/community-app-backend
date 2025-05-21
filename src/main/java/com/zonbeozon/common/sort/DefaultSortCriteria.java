package com.zonbeozon.common.sort;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DefaultSortCriteria {
    LAST_MODIFIED("modifiedAt"), LAST_CREATED("createdAt");

    private final String criteriaName;

    public static Sort get(final String criteriaName) {
        DefaultSortCriteria criteria = parse(criteriaName);
        return Sort.by(Sort.Direction.DESC, criteria.criteriaName);

    }

    private static DefaultSortCriteria parse(final String criteriaName) {
        return Arrays.stream(DefaultSortCriteria.values())
                .filter(c -> c.criteriaName.equals(criteriaName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown sort criteria: " + criteriaName));
    }
}
