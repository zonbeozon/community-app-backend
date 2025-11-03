package com.zonbeozon.config;

import com.zonbeozon.global.CookieService;
import com.zonbeozon.global.viewcount.CookieViewMarker;
import com.zonbeozon.post.repository.PostMetricRepositoryImpl;
import com.zonbeozon.post.service.metric.viewcount.LazyPostViewCounter;
import com.zonbeozon.post.service.metric.viewcount.PostViewCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewCounterConfig {
    @Bean
    public PostViewCounter postViewCounter(PostMetricRepositoryImpl postMetricRepositoryImpl) {
        return new LazyPostViewCounter(postMetricRepositoryImpl);
    }

    @Bean
    public CookieViewMarker postViewMarker(CookieService cookieService) {
        return new CookieViewMarker(cookieService, new CookieViewMarker.ViewCountDomainProperties("viewedPostIds"));
    }
}
