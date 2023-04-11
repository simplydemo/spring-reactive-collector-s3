package io.github.simplydemo.webfluxdatacollectors3;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataCollectorApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(DataCollectorApplicationTests.class);

	@Test
	void contextLoads() {
		logger.info("test");
	}

}
