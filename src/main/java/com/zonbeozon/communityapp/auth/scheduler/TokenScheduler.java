package com.zonbeozon.communityapp.auth.scheduler;

import com.zonbeozon.communityapp.auth.repository.TokenRepository;
import com.zonbeozon.communityapp.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenScheduler {
    private final TokenService tokenService;

    @Scheduled(cron = "${scheduler.auth.token}", zone = "${scheduler.default.timezone}")
    public void deleteExpiredTokens() {
        Date now = new Date();
        tokenService.deleteExpiredTokens(now);
    }
}
