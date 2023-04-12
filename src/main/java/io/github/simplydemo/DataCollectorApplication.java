package io.github.simplydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class,
        // JvmMetricsAutoConfiguration.class, LogbackMetricsAutoConfiguration.class, MetricsAutoConfiguration.class
})
public class DataCollectorApplication {

    public static void main(String[] args) {
//        final LogbackConfigure configure = new LogbackConfigure();
//        configure.create(
//                "DataCollector.default.ingest",
//                "ap-northeast-1",
//                "opsdev-sts",
//                "otcmp-tbd-artifact-s3",
//                "/tmp/logs/collector.log",
//                "/tmp/logs/collector.%d{yyyyMMdd-HHmm}.log",
//                "50MB",
//                "300MB",
//                3);
        SpringApplication.run(DataCollectorApplication.class, args);
    }

}
