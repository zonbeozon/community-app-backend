package com.zonbeozon.communityapp.crpyto.execute;

import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.update.Updater;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class Executor {
    private final List<Updater> updaters;
    private final ExchangeService exchangeService;

    @PostConstruct
    public void initExecute() {
        addDefaultExchanges();
        updaters.stream()
                .filter(updater -> updater.getExecuteTypes().contains(ExecuteType.ONE_TIME))
                .forEach(Updater::update);
    }

    @Scheduled(fixedDelay = 5000)
    public void fiveSecExecute() {
        updaters.stream()
                .filter(updater->updater.getExecuteTypes().contains(ExecuteType.FIVE_SEC))
                .forEach(Updater::update);
    }

    @Scheduled(fixedDelay = 3600000)
    public void OneHourExecute() {
        updaters.stream()
                .filter(updater->updater.getExecuteTypes().contains(ExecuteType.ONE_HOUR))
                .forEach(Updater::update);
    }


    private void addDefaultExchanges() {
        ExchangeRequest upbitExchangeRequest = new ExchangeRequest();
        upbitExchangeRequest.setKoreanName("업비트");
        upbitExchangeRequest.setEnglishName("upbit");
        upbitExchangeRequest.setDescription("업비트 거래소.");
        exchangeService.register(upbitExchangeRequest);

        ExchangeRequest bithumbExchangeRequest = new ExchangeRequest();
        bithumbExchangeRequest.setKoreanName("빗썸");
        bithumbExchangeRequest.setEnglishName("bithumb");
        bithumbExchangeRequest.setDescription("빗썸 거래소.");
        exchangeService.register(bithumbExchangeRequest);
    }

}
