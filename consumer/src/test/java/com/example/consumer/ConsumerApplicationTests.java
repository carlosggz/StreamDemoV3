package com.example.consumer;

import com.example.consumer.services.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.support.MessageBuilder;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest({
        "spring.cloud.stream.bindings.personsInputChannel-in-0.destination=persons-topic1",
        "spring.cloud.stream.bindings.othersInputChannel-in-0.destination=persons-topic2",
        "spring.cloud.stream.bindings.allInputChannel-in-0.destination=persons-topic3"
})
@EnableTestBinder
class ConsumerApplicationTests {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    StatsService service;

    @BeforeEach
    void setup() {
        service.reset();
    }

    @Test
    void contextLoads() {
    }

    @ParameterizedTest
    @MethodSource("cases")
    void messagesAreReceived(String topicName, int expectedFirst, int expectedSecond, int expectedThird) {

        inputDestination.send(MessageBuilder.withPayload("test").build(), topicName);
        assertEquals(expectedFirst, service.getFirst());
        assertEquals(expectedSecond, service.getSecond());
        assertEquals(expectedThird, service.getThird());
    }

    static Stream<Arguments> cases() {
        return Stream.of(
                Arguments.of("persons-topic1",  1, 0, 0),
                Arguments.of("persons-topic2",  0, 1, 0),
                Arguments.of("persons-topic3",  0, 0, 1)
        );
    }
}
