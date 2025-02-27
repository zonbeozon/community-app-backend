package com.zonbeozon.communityapp.crpyto.sse.entity;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class EntireMarketEmitter {
    private final SseEmitter emitter;
    private final Exchange exchange;
    private final FiatType fiatType;

    public void onTimeout(Runnable callback) {
        emitter.onTimeout(callback);
    }

    public void onCompletion(Runnable callback) {
        emitter.onCompletion(callback);
    }

    public void onError(Consumer<Throwable> callback) {
        emitter.onError(callback);
    }

    public void complete() {
        emitter.complete();
    }

    public void send(Object object) throws IOException {
        emitter.send(object);
    }
}
