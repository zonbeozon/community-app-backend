package com.zonbeozon.global.apiclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncRestClient {
    private final TaskExecutor taskExecutor;
    private final RestClient restClient;

    public AsyncRestClient(
            @Qualifier("apiClientTaskExecutor")TaskExecutor taskExecutor,
            RestClient restClient
            ) {
        this.taskExecutor = taskExecutor;
        this.restClient = restClient;
    }

    public <T> CompletableFuture<T> execute(ApiCallTask<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        taskExecutor.execute(() -> {
            try {
                T result = task.call(restClient);
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }
}
