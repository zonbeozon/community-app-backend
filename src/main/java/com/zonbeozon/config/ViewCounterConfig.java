package com.zonbeozon.config;

import com.zonbeozon.global.CookieService;
import com.zonbeozon.global.viewcount.CookieViewMarker;
import com.zonbeozon.post.service.viewcount.LazyPostViewCounter;
import com.zonbeozon.post.service.viewcount.PostViewCounter;
import com.zonbeozon.post.repository.PostRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewCounterConfig {
    @Bean
    public PostViewCounter postViewCounter(PostRepository postRepository) {
        return new LazyPostViewCounter(postRepository);
    }

    @Bean
    public CookieViewMarker postViewMarker(CookieService cookieService) {
        return new CookieViewMarker(cookieService, new CookieViewMarker.ViewCountDomainProperties("viewedPostIds"));
    }
}
