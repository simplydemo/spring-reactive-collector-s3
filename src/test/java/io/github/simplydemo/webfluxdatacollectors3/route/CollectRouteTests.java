package io.github.simplydemo.webfluxdatacollectors3.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.simplydemo.webfluxdatacollectors3.DataCollectorApplication;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = DataCollectorApplication.class)
public class CollectRouteTests {

    private final Logger logger = LoggerFactory.getLogger(CollectRouteTests.class);

    @Autowired
    private RouteConfiguration config;

    @Test
    void collectRoute_postHandle() {
        WebTestClient client = WebTestClient.bindToRouterFunction(config.collectRoute()).build();

        String payload = "{\"id\": 1, \"name\": \"apple\", \"custNo\": \"A1001\", \"age\": 20, \"gender\": \"F\"}";

        client.post().uri("/api/collect").bodyValue(payload).exchange().expectStatus().isCreated();
    }


}
