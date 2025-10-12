package com.zonbeozon.global.apiclient;

import org.springframework.web.client.RestClient;

@FunctionalInterface
public interface ApiCallTask<T> {
    T call(RestClient restClient);

}
