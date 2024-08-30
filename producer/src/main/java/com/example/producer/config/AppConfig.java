package com.example.producer.config;

import com.example.producer.components.PersonsPublisher;
import com.example.producer.dtos.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;
import java.util.stream.Stream;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AppConfig {
    private final PersonsPublisher personsPublisher;

    @Bean
    public Supplier<Flux<Message<Person>>> personsSupplier() {
        return personsPublisher::getFlux;
    }

    @Scheduled(fixedDelay=3000)
    public void publishPersons() {
        Stream
                .of("first.message.key", "second.message.key", "third.message.key")
                .forEach(personsPublisher::publish);
    }
}
