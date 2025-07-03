package com.zonbeozon.post.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class NoOpEventPublisherTestConfig {
    @Bean
    public ApplicationEventPublisher applicationEventPublisher() {
        ApplicationEventPublisher noOpEventPublisher =  new ApplicationEventPublisher() {
            @Override
            public void publishEvent(ApplicationEvent event) {
            }

            @Override
            public void publishEvent(Object event) {
            }
        };

        return noOpEventPublisher;
    }
}