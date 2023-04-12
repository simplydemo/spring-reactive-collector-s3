package io.github.simplydemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class,
        // JvmMetricsAutoConfiguration.class, LogbackMetricsAutoConfiguration.class, MetricsAutoConfiguration.class
})
public class DataCollectorApplication {

    private final Logger logger = LoggerFactory.getLogger(DataCollectorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DataCollectorApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> health() {
        return RouterFunctions.route().GET("/health", this::handleHealth).build();
    }

    private Mono<ServerResponse> handleHealth(ServerRequest request) {
        logger.debug("handleHealth");
        return request.bodyToMono(String.class)
                .doOnNext(requestBody -> {
                })
                .then(ok().bodyValue("OK"));
    }
}
