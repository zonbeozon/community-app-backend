package com.zonbeozon.communityapp.crpyto.update;

import com.zonbeozon.communityapp.crpyto.execute.ExecuteType;
import com.zonbeozon.communityapp.crpyto.fetch.FetchManager;
import com.zonbeozon.communityapp.crpyto.fetch.MarketFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.TickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TickerUpdater implements Updater {
    private static final Set<ExecuteType> executeTypes = Set.of(ExecuteType.FIVE_SEC);

    private final FetchManager fetchManager;
    private final TickerService tickerService;

    @Override
    public void update() {
        fetchManager.fetchTickers().forEach(tickerService::updateTickers);
    }

    @Override
    public Set<ExecuteType> getExecuteTypes() {
        return executeTypes;
    }
}
