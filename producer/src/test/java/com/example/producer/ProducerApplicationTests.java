package com.example.producer;

import com.example.producer.components.PersonsPublisher;
import com.example.producer.dtos.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableTestBinder
class ProducerApplicationTests {

    private static final String TOPIC_NAME = "persons-topic";
    private static final String TEST_BINDING = "test-in-0";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private Supplier<Flux<Message<Person>>> personsSupplier;

    @Autowired
    private StreamBridge streamBridge;

    @Test
    void contextLoads() {
    }

    @ParameterizedTest
    @ValueSource(strings = {"first.message.key", "second.message.key", "third.message.key"})
    void producersSendMessages(String routingKey) {
        outputDestination.clear(TOPIC_NAME);
        emulateSubscription();

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    val message = outputDestination.receive(1000, TOPIC_NAME);
                    assertNotNull(message);
                    assertEquals(routingKey, message.getHeaders().get(PersonsPublisher.ROUTING_KEY));
                    val person = objectMapper.readValue(message.getPayload(), Person.class);
                    assertNotNull(person);
                    System.out.println(person);
                });
    }

    private void emulateSubscription() {
        /* Binding is not running because it has no subscriptions, so, I emulate its behavior */
        personsSupplier
                .get()
                .doOnNext(x -> streamBridge.send(TEST_BINDING, x))
                .subscribe();
    }
}
