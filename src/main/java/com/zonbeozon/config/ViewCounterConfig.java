package com.zonbeozon.config;

import com.zonbeozon.global.CookieService;
import com.zonbeozon.global.viewcount.CookieViewMarker;
import com.zonbeozon.global.viewcount.LazyViewCounter;
import com.zonbeozon.global.viewcount.ViewCounter;
import com.zonbeozon.post.service.PostFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewCounterConfig {
    @Bean
    public ViewCounter postViewCounter(PostFinder postFinder) {
        return new LazyViewCounter(postFinder);
    }

    @Bean
    public CookieViewMarker postViewMarker(CookieService cookieService) {
        return new CookieViewMarker(cookieService, new CookieViewMarker.ViewCountDomainProperties("viewedPostIds"));
    }
}
