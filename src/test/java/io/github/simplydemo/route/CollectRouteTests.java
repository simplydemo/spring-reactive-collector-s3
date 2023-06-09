package io.github.simplydemo.route;

import io.github.simplydemo.DataCollectorApplication;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

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
