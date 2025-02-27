package com.zonbeozon.communityapp.crpyto.sse.repository;

import com.zonbeozon.communityapp.crpyto.sse.entity.EntireMarketEmitter;
import com.zonbeozon.communityapp.crpyto.sse.entity.SingleMarketEmitter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SingleMarketEmitterRepository {

    private final List<SingleMarketEmitter> singleEntireMarketEmitters = new CopyOnWriteArrayList<>();

    public SingleMarketEmitter save(SingleMarketEmitter emitter) {
        singleEntireMarketEmitters.add(emitter);
        return emitter;
    }

    public void delete(SingleMarketEmitter emitter) {
        singleEntireMarketEmitters.remove(emitter);
    }

    public List<SingleMarketEmitter> findAll() {
        return singleEntireMarketEmitters;
    }
}
