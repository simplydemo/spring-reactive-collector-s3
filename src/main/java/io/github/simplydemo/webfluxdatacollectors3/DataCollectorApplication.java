package io.github.simplydemo.webfluxdatacollectors3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;


@SpringBootApplication(exclude = {JacksonAutoConfiguration.class,
		// JvmMetricsAutoConfiguration.class, LogbackMetricsAutoConfiguration.class, MetricsAutoConfiguration.class
})
public class DataCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataCollectorApplication.class, args);
	}

}
