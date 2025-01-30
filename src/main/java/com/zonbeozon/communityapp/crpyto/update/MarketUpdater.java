package com.zonbeozon.communityapp.crpyto.update;

import com.zonbeozon.communityapp.crpyto.execute.ExecuteType;
import com.zonbeozon.communityapp.crpyto.fetch.FetchManager;
import com.zonbeozon.communityapp.crpyto.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MarketUpdater implements Updater {
    private static final Set<ExecuteType> executeTypes = Set.of(ExecuteType.ONE_TIME, ExecuteType.ONE_HOUR);

    private final MarketService marketService;
    private final FetchManager fetchManager;

    @Override
    public void update() {
        fetchManager.fetchMarkets().forEach(marketService::updateMarkets);
    }

    @Override
    public Set<ExecuteType> getExecuteTypes() {
        return executeTypes;
    }
}
