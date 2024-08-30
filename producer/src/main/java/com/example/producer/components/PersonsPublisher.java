package com.example.producer.components;

import com.example.producer.dtos.Person;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class PersonsPublisher {
    public static final String ROUTING_KEY = "myRoutingKey";
    private static final int BUFFER_SIZE = 1024;
    private final Sinks.Many<Message<Person>> subject = Sinks
            .many()
            .multicast()
            .onBackpressureBuffer(BUFFER_SIZE, false);

    public synchronized void publish(String routingKey) {
        val id = UUID.randomUUID().toString();
        val message = MessageBuilder
                .withPayload(new Person(id, routingKey + "-" + id, LocalDateTime.now().getSecond()))
                .setHeader(ROUTING_KEY, routingKey)
                .build();
        log.info("Sending person: {}", message.getPayload());
        subject.emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public Flux<Message<Person>> getFlux() {
        return subject.asFlux();
    }
}
