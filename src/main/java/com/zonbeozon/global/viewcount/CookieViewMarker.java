package com.zonbeozon.global.viewcount;

import com.zonbeozon.global.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class CookieViewMarker {
    private static final int ONE_DAY_IN_SECONDS = 60 * 60 * 24;
    private static final String DELIMITER = "_";
    private static final int MAX_COOKIE_VALUE_SIZE = 50;
    private static final String VIEW_HISTORY_ATTRIBUTE = "viewHistory";

    private final CookieService cookieService;
    private final ViewCountDomainProperties viewCountDomainProperties;

    public boolean hasViewed(HttpServletRequest request, Long contentId) {
        return getViewContentIdFromCookie(request).stream()
                .anyMatch(id -> id.equals(contentId));
    }

    public void setAsViewed(HttpServletRequest request, HttpServletResponse response, List<Long> contentIds) {
        List<Long> viewedContentIds = getViewContentIdFromCookie(request);
        viewedContentIds.addAll(contentIds);
        while (viewedContentIds.size() > MAX_COOKIE_VALUE_SIZE) {
            viewedContentIds.removeFirst(); // 가장 오래된 인덱스 부터 제거
        }
        updateCookie(request, response, viewedContentIds);
    }

    @SuppressWarnings("unchecked")
    private List<Long> getViewContentIdFromCookie(HttpServletRequest request) {
        // request attribute에 이미 파싱된 결과가 있는지 확인
        Object cachedHistory = request.getAttribute(VIEW_HISTORY_ATTRIBUTE);
        if (cachedHistory instanceof List) {
            return (List<Long>) cachedHistory;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return new ArrayList<>();
        }
        List<Long> viewedContentIds = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(viewCountDomainProperties.cookieName()))
                .findFirst()
                .map(Cookie::getValue)
                .map(value -> {
                    try {
                        return Arrays.stream(value.split(DELIMITER))
                                .filter(s -> !s.isEmpty())
                                .map(Long::parseLong)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } catch (NumberFormatException e) {
                        log.debug("Failed to parse cookie, return new empty set", e);
                        return new ArrayList<Long>();
                    }
                })
                .orElseGet(ArrayList::new);

        request.setAttribute(VIEW_HISTORY_ATTRIBUTE, viewedContentIds); // 여러번 호출을 대비해 캐싱
        return viewedContentIds;
    }

    private void updateCookie(HttpServletRequest request, HttpServletResponse response, List<Long> viewHistories) {
        List<String> castedViewHistories = viewHistories.stream().map(id -> Long.toString(id)).toList();
        String newCookieValue = String.join(DELIMITER, castedViewHistories);
        Cookie newCookie = cookieService.createCookie(viewCountDomainProperties.cookieName(), newCookieValue, ONE_DAY_IN_SECONDS);
        response.addCookie(newCookie);
        request.removeAttribute(VIEW_HISTORY_ATTRIBUTE); // 무효화
    }

    public record ViewCountDomainProperties(
            String cookieName
    ) {}

}
