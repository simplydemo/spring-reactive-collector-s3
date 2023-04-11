package io.github.simplydemo.webfluxdatacollectors3.route;

import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WebClientTests {

    private final Logger logger = LoggerFactory.getLogger(WebClientTests.class);


    @Test
    void test_request() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        final WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                //.defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        final Random rand = new Random();
        for (int i = 0; i < 3000; i++) {
            final String name = Long.toString(Math.abs(rand.nextLong() % 3656158440062976L), 36);
            try {
                User user = new User();
                user.setId(String.format("I100%d", i));
                user.setName(name);
                user.setBirthday(LocalDate.of(rand.nextInt(2001 - 1976 + 1) + 1976, rand.nextInt(12 - 1 + 1) + 1, rand.nextInt(30 - 1 + 1) + 1));
                user.setHeight(rand.nextInt(190 - 165 + 1) + 165);
                user.setWeight(rand.nextInt(100 - 60 + 1) + 60);
                user.setTimestamp(Instant.now().toEpochMilli());
                String payload = mapper.writeValueAsString(user);

                var status = client
                        .post().uri("/api/collect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(user), User.class)
                        //.bodyValue(payload)
                        .exchangeToMono(response -> {
                            return Mono.just(response.statusCode());
                        }).block();
                System.out.println(i + " --- " + status.value() + " --- " + payload);
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }


    @Test
    public void test_filename() {
        FileNamePattern f = new FileNamePattern("%d{yyyyMMdd}/", new ContextBase());
        System.out.println(f.getPrimaryDateTokenConverter().convert(Instant.now()));
        System.out.println(f.getPrimaryDateTokenConverter().convert(Instant.ofEpochMilli(1681231777081L)));
    }

}
