package com.zonbeozon.communityapp.crpyto.sse.aspect;

import com.zonbeozon.communityapp.crpyto.service.market.MarketResponseFactory;
import com.zonbeozon.communityapp.crpyto.sse.service.EntireMarketEmitterService;
import com.zonbeozon.communityapp.crpyto.sse.service.SingleMarketEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TickerUpdateAspect {

    private final EntireMarketEmitterService entireEmitterService;
    private final SingleMarketEmitterService singleMarketEmitterService;
    private final MarketResponseFactory marketResponseFactory;

    /**
     * Ticker 업데이트 후 전체 시장 정보를 SSE로 전송
     */
    @AfterReturning("execution(* com.zonbeozon.communityapp.crpyto.schedule.MarketScheduler.run(..))")
    public void afterTickerUpdate() {
        log.debug("마켓 업데이트 완료 -> SSE 데이터 전송 시작");
        sendMarketInfo();
    }

    private void sendMarketInfo() {
        entireEmitterService.sendToAll(marketResponseFactory.createEntireMarketInfoResponsesMap());
        singleMarketEmitterService.sendToAll(marketResponseFactory.createSingleMarketInfoResponsesMap());
    }
}
