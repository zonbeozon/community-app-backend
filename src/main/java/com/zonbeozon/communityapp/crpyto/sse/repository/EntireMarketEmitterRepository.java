package com.zonbeozon.communityapp.crpyto.sse.repository;

import com.zonbeozon.communityapp.crpyto.sse.entity.EntireMarketEmitter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class EntireMarketEmitterRepository {
    private final List<EntireMarketEmitter> entireEntireMarketEmitters = new CopyOnWriteArrayList<>();

    public EntireMarketEmitter save(EntireMarketEmitter emitter) {
        entireEntireMarketEmitters.add(emitter);
        return emitter;
    }

    public void delete(EntireMarketEmitter emitter) {
        entireEntireMarketEmitters.remove(emitter);
    }

    public List<EntireMarketEmitter> findAll() {
        return entireEntireMarketEmitters;
    }
}
