package com.example.consumer.config;

import com.example.consumer.services.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {
    private final StatsService service;

    @Bean
    public Consumer<Flux<Message<String>>> personsInputChannel() {
        return flux -> flux
                .subscribe(message -> {
                    showLog("personsInputChannel", message.getPayload());
                    service.incFirst();
                });
    }

    @Bean
    public Consumer<Flux<Message<String>>> othersInputChannel() {
        return flux -> flux
                .subscribe(message -> {
                    showLog("othersInputChannel", message.getPayload());
                    service.incSecond();
                });
    }

    @Bean
    public Consumer<Flux<Message<String>>> allInputChannel() {
        return flux -> flux
                .subscribe(message -> {
                    showLog("allInputChannel", message.getPayload());
                    service.incThird();
                });
    }

    private void showLog(String channel, String message) {
        log.info("Received person on channel {}: {}, on thread {}", channel, message, Thread.currentThread().getName());
    }
}
