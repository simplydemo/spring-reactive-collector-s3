package io.github.simplydemo.webfluxdatacollectors3.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Configuration
public class RouteConfiguration {

    private final Logger logger = LoggerFactory.getLogger("DataCollector.default.ingest");

    private final ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, false);

    @Bean
    RouterFunction<ServerResponse> collectRoute() {
        System.out.println("collectRoute()");
        return RouterFunctions.route().POST("/api/collect", this::handlePost).build();
    }

    private Mono<ServerResponse> handlePost(ServerRequest request) {
        return request.bodyToMono(String.class).doOnNext(requestBody -> {
            final String json = new String(requestBody.getBytes(StandardCharsets.UTF_8));
            try {
                final JsonNode jsonNode = mapper.readTree(json);
                var row = mapper.writeValueAsString(jsonNode);
                logger.info(row);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).then(status(HttpStatus.CREATED).build());
    }
}
